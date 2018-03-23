package com.william.devx.common.interceptor;

import com.william.devx.common.Resp;

/**
 * 拦截器栈定义
 *
 * @param <I> 输入对象的类型
 * @param <O> 输出对象的类型
 */
public interface DevxInterceptor<I, O> {

    /**
     * 获取拦截器所属类型，用于区别不同的栈
     */
    String getCategory();

    /**
     * 获取拦截器名称
     */
    String getName();

    /**
     * 前置执行
     *
     * @param context 操作上下文
     * @return 执行后结果
     */
    Resp<DevxInterceptContext<I, O>> before(DevxInterceptContext<I, O> context);

    /**
     * 后置执行
     *
     * @param context 操作上下文
     * @return 执行后结果
     */
    Resp<DevxInterceptContext<I, O>> after(DevxInterceptContext<I, O> context);

    /**
     * 错误处理，在前置/后置执行错误时触发，多用于资源回收
     *
     * @param context 操作上下文
     */
    default void error(DevxInterceptContext<I, O> context) {
        // Do Nothing.
    }

}
