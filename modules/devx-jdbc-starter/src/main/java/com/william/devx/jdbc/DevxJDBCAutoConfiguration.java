package com.william.devx.jdbc;

import com.william.devx.Devx;
import com.william.devx.core.annotation.DevxLoadImmediately;
import com.william.devx.jdbc.config.DevxJDBCConfig;
import com.william.devx.jdbc.entity.EntityContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;

import javax.annotation.PostConstruct;

@Configuration
@DevxLoadImmediately
@Slf4j
public class DevxJDBCAutoConfiguration {


    @Autowired
    private DevxJDBCConfig dewJDBCConfig;

    @PostConstruct
    private void init() {
        log.info("Enabled Devx JDBC");
        Devx.applicationContext.containsBean(EntityContainer.class.getSimpleName());
        // JDBC Scan
        if (!dewJDBCConfig.getJdbc().getBasePackages().isEmpty()) {
            ClassPathScanner scanner = new ClassPathScanner((BeanDefinitionRegistry) ((GenericApplicationContext) Devx.applicationContext).getBeanFactory());
            scanner.setResourceLoader(Devx.applicationContext);
            scanner.registerFilters();
            scanner.scan(dewJDBCConfig.getJdbc().getBasePackages().toArray(new String[]{}));
        }
    }


}