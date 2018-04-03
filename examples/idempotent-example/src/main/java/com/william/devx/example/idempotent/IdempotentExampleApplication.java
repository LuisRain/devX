package com.william.devx.example.idempotent;


import com.william.devx.Devx;
import com.william.devx.core.autoconfigure.DevxBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;


@DevxBootApplication(scanBasePackageClasses = {Devx.class, IdempotentExampleApplication.class})
public class IdempotentExampleApplication{

    public static void main(String[] args) {
        new SpringApplicationBuilder(IdempotentExampleApplication.class).run(args);
    }
}
