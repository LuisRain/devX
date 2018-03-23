package com.william.devx.logging.core;



import com.william.devx.logging.api.LoggerDefine;
import com.william.devx.logging.core.aop.MethodInterceptorHolder;

import java.lang.reflect.Method;

public interface AccessLoggerParser {
    boolean support(Class clazz, Method method);

    LoggerDefine parse(MethodInterceptorHolder holder);
}
