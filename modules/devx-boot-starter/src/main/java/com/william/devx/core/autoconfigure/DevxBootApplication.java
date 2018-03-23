package com.william.devx.core.autoconfigure;

import com.william.devx.Devx;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.annotation.AliasFor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootApplication
@EnableTransactionManagement
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@EnableCaching(proxyTargetClass = true)
@DependsOn("devx")
public @interface DevxBootApplication {

    @AliasFor(annotation = SpringBootApplication.class, attribute = "exclude")
    Class<?>[] exclude() default {FreeMarkerAutoConfiguration.class, GsonAutoConfiguration.class, WebSocketServletAutoConfiguration.class};

    @AliasFor(annotation = SpringBootApplication.class, attribute = "scanBasePackages")
    String[] scanBasePackages() default {};

    @AliasFor(annotation = SpringBootApplication.class, attribute = "scanBasePackageClasses")
    Class<?>[] scanBasePackageClasses() default {Devx.class,};

}
