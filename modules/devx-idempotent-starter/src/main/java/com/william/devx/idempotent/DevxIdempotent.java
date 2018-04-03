package com.william.devx.idempotent;

import com.william.devx.idempotent.strategy.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class DevxIdempotent {

    // optType -> DevxIdempotentProcessor
    private static final Map<String, OptTypeInfo> CONTENT = new HashMap<>();

    // optType,optId
    private static final ThreadLocal<String[]> CONTEXT = new ThreadLocal<>();

    @Autowired
    @Qualifier("itemProcessor")
    private ItemProcessor innerItemProcessor;

    @Autowired
    @Qualifier("bloomFilterProcessor")
    private BloomFilterProcessor innerBloomFilterProcessor;

    private static ItemProcessor itemProcessor;
    private static BloomFilterProcessor bloomFilterProcessor;

    @PostConstruct
    private void init() {
        itemProcessor = innerItemProcessor;
        bloomFilterProcessor = innerBloomFilterProcessor;
    }

    /**
     * 初始化操作类型信息
     *
     * @param optType     操作类型
     * @param needConfirm 是否需要显式确认
     * @param expireMs    过期时间
     * @param strategy    策略类型
     */
    public static void initOptTypeInfo(String optType, boolean needConfirm, long expireMs, StrategyEnum strategy) {
        log.info("Init OptType[{}] info: needConfirm:{} expireMs:{} strategy:{}", optType, needConfirm, expireMs, strategy.toString());
        DevxIdempotentProcessor processor;
        switch (strategy) {
            case ITEM:
                processor = itemProcessor;
                break;
            case BLOOM_FLTER:
                processor = bloomFilterProcessor;
                break;
            default:
                log.error("Init OptType[{}] error: strategy:{} NOT exist.", strategy.toString());
                return;
        }
        OptTypeInfo optTypeInfo = new OptTypeInfo();
        optTypeInfo.processor = processor;
        optTypeInfo.expireMs = expireMs;
        optTypeInfo.needConfirm = needConfirm;
        CONTENT.put(optType, optTypeInfo);
    }


    /**
     * 操作类型是否存在，用于初化判断
     *
     * @param optType 操作类型
     * @return 是否存在
     */
    public static boolean existOptTypeInfo(String optType) {
        return CONTENT.containsKey(optType);
    }

    /**
     * 持久化
     *
     * @param optType 操作类型
     * @param optId   操作ID
     */
    public static StatusEnum process(String optType, String optId) {
        CONTEXT.set(new String[]{optType, optId});
        OptTypeInfo optTypeInfo = CONTENT.get(optType);
        return optTypeInfo.processor.process(optType, optId, optTypeInfo.needConfirm ? StatusEnum.UN_CONFIRM : StatusEnum.CONFIRMED, optTypeInfo.expireMs);
    }

    /**
     * 操作确认
     *
     * @param optType 操作类型
     * @param optId   操作ID
     */
    public static void confirm(String optType, String optId) {
        CONTENT.get(optType).processor.confirm(optType, optId);
    }

    /**
     * 操作确认，要求与请求入口在同一线程中
     */
    public static void confirm() {
        String[] c = CONTEXT.get();
        CONTENT.get(c[0]).processor.confirm(c[0], c[1]);
    }

    /**
     * 操作取消
     *
     * @param optType 操作类型
     * @param optId   操作ID
     */
    public static void cancel(String optType, String optId) {
        CONTENT.get(optType).processor.cancel(optType, optId);
    }

    /**
     * 操作取消，要求与请求入口在同一线程中
     */
    public static void cancel() {
        String[] c = CONTEXT.get();
        CONTENT.get(c[0]).processor.cancel(c[0], c[1]);
    }

    private static class OptTypeInfo {

        public DevxIdempotentProcessor processor;
        public long expireMs;
        public boolean needConfirm;

    }

}
