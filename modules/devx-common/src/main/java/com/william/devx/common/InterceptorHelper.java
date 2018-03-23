package com.william.devx.common;

import com.william.devx.common.interceptor.DevxInterceptContext;
import com.william.devx.common.interceptor.DevxInterceptExec;
import com.william.devx.common.interceptor.DevxInterceptor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 拦截器栈执行器
 */
@Slf4j
public class InterceptorHelper {


    private static Map<String, List<DevxInterceptor<?, ?>>> CONTAINER = new HashMap<>();

    InterceptorHelper() {
    }

    /**
     * 注册拦截器栈
     *
     * @param category    拦截类型
     * @param interceptor 拦截器
     */
    public void register(String category, DevxInterceptor<?, ?> interceptor) {
        if (!CONTAINER.containsKey(category)) {
            CONTAINER.put(category, new ArrayList<>());
        }
        CONTAINER.get(category).add(interceptor);
    }

    /**
     * 注册拦截器栈
     *
     * @param category     拦截类型
     * @param interceptors 拦截器列表
     */
    public void register(String category, List<DevxInterceptor<?, ?>> interceptors) {
        CONTAINER.put(category, interceptors);
    }

    /**
     * 拦截器栈处理方法
     *
     * @param category 拦截类型
     * @param input    初始入栈对象
     * @param fun      实际执行方法
     * @tparam E 对象的类型
     */
    public <I, O> Resp<DevxInterceptContext<I, O>> process(String category, I input, DevxInterceptExec<I, O> fun) {
        return process(category, input, new HashMap<>(), fun);
    }

    /**
     * 拦截器栈处理方法
     *
     * @param category 拦截类型
     * @param input    初始入栈对象
     * @param args     初始入栈参数
     * @param fun      实际执行方法
     * @tparam E 对象的类型
     */
    public <I, O> Resp<DevxInterceptContext<I, O>> process(String category, I input, Map<String, Object> args, DevxInterceptExec<I, O> fun) {
        DevxInterceptContext<I, O> context = new DevxInterceptContext<>();
        context.setInput(input);
        context.setArgs(args);
//        log.debug("[DevxInterceptorProcessor] Process [{}]", category);
        if (!CONTAINER.containsKey(category)) {
            return fun.exec(context);
        }
        List<DevxInterceptor<?, ?>> interceptors = CONTAINER.get(category);
        Resp<DevxInterceptContext<I, O>> beforeR = doProcess(context, interceptors, true);
        if (!beforeR.ok()) {
            return beforeR;
        }
        Resp<DevxInterceptContext<I, O>> execR = fun.exec(beforeR.getBody());
        if (!execR.ok()) {
            return execR;
        }
        return doProcess(execR.getBody(), interceptors, false);
    }

    private static <I, O> Resp<DevxInterceptContext<I, O>> doProcess(DevxInterceptContext<I, O> context, List<DevxInterceptor<?, ?>> interceptors, boolean isBefore) {
        Resp<DevxInterceptContext<I, O>> result = Resp.success(context);
        for (DevxInterceptor<?, ?> interceptor : interceptors) {
//            logger.trace("[DevxInterceptorProcessor] Process interceptor [{}]:{}-{}", interceptor.getCategory(), interceptor.getName(), isBefore ? "before" : "after");
            DevxInterceptor<I, O> interceptorE = (DevxInterceptor<I, O>) interceptor;
            try {
                if (isBefore) {
                    result = interceptorE.before(context);
                } else {
                    result = interceptorE.after(context);
                }
                if (!result.ok()) {
//                    logger.warn("[DevxInterceptorProcessor] Process interceptor error [{}]:{}-{},[{}]{}",
//                            interceptor.getCategory(), interceptor.getName(), isBefore ? "before" : "after", result.getCode(), result.getMessage());
                    interceptorE.error(context);
                    return result;
                }
            } catch (Throwable e) {
                result = Resp.serverError(e.getMessage());
//                logger.error("[DevxInterceptorProcessor] Process interceptor error [{}]:{}-{},[{}]{}",
//                        interceptor.getCategory(), interceptor.getName(), isBefore ? "before" : "after", result.getCode(), result.getMessage());
                interceptorE.error(context);
                return result;
            }
        }
        return result;
    }

}
