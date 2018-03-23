package com.william.devx.cache.example;

import com.william.devx.Devx;
import com.william.devx.core.DevxBootApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by sungang on 2017/11/3.
 */
@Slf4j
@ComponentScan(basePackageClasses = {Devx.class, CacheExampleApplication.class})
public class CacheExampleApplication extends DevxBootApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(CacheExampleApplication.class).run(args);
    }
}
