package com.william.devx.mybatisplus;


import com.william.devx.Devx;
import com.william.devx.core.DevxBootApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

/**
 * mybatis-plus Spring Boot 测试 Demo<br>
 */
@ComponentScan(basePackageClasses = {Devx.class, ExampleMybatisPlusApplication.class})
@Slf4j
public class ExampleMybatisPlusApplication extends DevxBootApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ExampleMybatisPlusApplication.class).run(args);
    }

}
