package com.william.devx.webflux;

import com.didispace.swagger.EnableSwagger2Doc;
import com.william.devx.Devx;
import com.william.devx.core.DevxBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * Hello world!
 */
@ComponentScan(basePackageClasses = {Devx.class, WebFluxExampleApplication.class})
@MapperScan("com.william.devx.webflux.mapper")
@EnableSwagger2Doc
public class WebFluxExampleApplication extends DevxBootApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(WebFluxExampleApplication.class).run(args);
    }
}
