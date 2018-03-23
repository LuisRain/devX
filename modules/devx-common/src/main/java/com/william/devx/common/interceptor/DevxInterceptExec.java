package com.william.devx.common.interceptor;


import com.william.devx.common.Resp;

@FunctionalInterface
public interface DevxInterceptExec<I, O> {

    Resp<DevxInterceptContext<I, O>> exec(DevxInterceptContext<I, O> context);

}
