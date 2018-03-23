package com.william.devx.example.jdbc;

import com.william.devx.Devx;
import com.william.devx.core.autoconfigure.DevxBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * 工程启动类
 */
@DevxBootApplication(scanBasePackageClasses = {Devx.class, JDBCExampleApplication.class})
public class JDBCExampleApplication {

    public static void main(String[] args) throws InterruptedException {
        new SpringApplicationBuilder(JDBCExampleApplication.class).run(args);
    }

}
