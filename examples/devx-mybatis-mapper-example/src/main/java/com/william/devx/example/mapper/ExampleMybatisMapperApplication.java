package com.william.devx.example.mapper;

import com.didispace.swagger.EnableSwagger2Doc;
import com.william.devx.Devx;
import com.william.devx.core.DevxBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * Created by sungang on 2018/3/16.
 */
@ComponentScan(basePackageClasses = {Devx.class, ExampleMybatisMapperApplication.class})
@EnableSwagger2Doc
@MapperScan(basePackages = "com.william.devx.example.mapper.mapper")
public class ExampleMybatisMapperApplication extends DevxBootApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ExampleMybatisMapperApplication.class).run(args);
    }
}
