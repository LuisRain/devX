package com.william.devx.core.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 不需要注入攻击拦截注解
 * 控制器中加入该注解,这个Mapping则不会进行注入拦截处理
 * Created by sungang on 2018/3/20.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface PassInjectionAttackIntercept {

    /**
     * 忽略的字符
     */
    @AliasFor("ignoreStrings")
    String[] value() default {};

    @AliasFor("value")
    String[] ignoreStrings() default {};
}
