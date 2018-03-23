package com.william.devx;

import com.didispace.swagger.EnableSwagger2Doc;
import com.william.devx.core.DevxBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

/**
 * Hello world!
 */
@ComponentScan(basePackageClasses = {Devx.class, WebExampleApplication.class})
@EnableSwagger2Doc
public class WebExampleApplication extends DevxBootApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(WebExampleApplication.class).run(args);
    }
}
