package com.william.devx.idempotent.strategy;


import com.william.devx.Devx;
import org.springframework.stereotype.Component;

@Component
public class ItemProcessor implements DevxIdempotentProcessor {

    private static final String CACHE_KEY = "devx:idempotent:item:";

    @Override
    public StatusEnum process(String optType, String optId, StatusEnum initStatus, long expireMs) {
        if (Devx.cluster.cache.setnx(CACHE_KEY + optType + ":" + optId, initStatus.toString(), expireMs / 1000)) {
            // 设置成功，表示之前不存在
            return StatusEnum.NOT_EXIST;
        } else {
            // 设置不成功，表示之前存在，返回存在的值
            return StatusEnum.valueOf(Devx.cluster.cache.get(CACHE_KEY + optType + ":" + optId));
        }
    }

    @Override
    public boolean confirm(String optType, String optId) {
        long ttl = Devx.cluster.cache.ttl(CACHE_KEY + optType + ":" + optId);
        if (ttl > 0) {
            Devx.cluster.cache.setex(CACHE_KEY + optType + ":" + optId, StatusEnum.CONFIRMED.toString(), ttl);
        }
        return true;
    }

    @Override
    public boolean cancel(String optType, String optId) {
        Devx.cluster.cache.del(CACHE_KEY + optType + ":" + optId);
        return true;
    }

}
