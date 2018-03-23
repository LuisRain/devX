package com.william.devx.auth;

import com.didispace.swagger.EnableSwagger2Doc;
import com.william.devx.Devx;
import com.william.devx.core.DevxBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by sungang on 2018/3/19.
 */
@ComponentScan(basePackageClasses = {Devx.class, AuthExampleApplication.class})
@EnableSwagger2Doc
public class AuthExampleApplication extends DevxBootApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(AuthExampleApplication.class).run(args);
    }

}
