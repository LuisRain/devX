package com.william.devx.core.annotation;

import java.lang.annotation.*;

/**
 * Created by sungang on 2018/3/16.
 * Devx 专用的Bean加载注解，Devx.class初始化立即加载对应的Bean
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DevxLoadImmediately {

}
