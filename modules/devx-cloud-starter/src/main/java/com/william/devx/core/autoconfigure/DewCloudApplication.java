package com.william.devx.core.autoconfigure;

import com.william.devx.Devx;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringCloudApplication
@DevxBootApplication
public @interface DewCloudApplication {

    @AliasFor(annotation = SpringBootApplication.class, attribute = "exclude")
    Class<?>[] exclude() default {FreeMarkerAutoConfiguration.class, GsonAutoConfiguration.class};

    @AliasFor(annotation = SpringBootApplication.class, attribute = "scanBasePackages")
    String[] scanBasePackages() default {};

    @AliasFor(annotation = SpringBootApplication.class, attribute = "scanBasePackageClasses")
    Class<?>[] scanBasePackageClasses() default {Devx.class,};

}
