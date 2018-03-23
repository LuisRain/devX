package com.william.devx.core.autoconfigure;

import ch.qos.logback.classic.Level;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;
import com.william.devx.core.DevxCloudConfig;
import com.william.devx.core.annotation.DevxLoadImmediately;
import com.william.devx.core.logger.DevxLoggerWebMvcConfigurer;
import com.william.devx.core.logger.DevxTraceLogWrap;
import com.william.devx.core.logger.DevxTraceRestTemplateInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Configuration
@DevxLoadImmediately
public class CloudAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(CloudAutoConfiguration.class);

    @Autowired
    private DevxCloudConfig devxCloudConfig;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired(required = false)
    private DevxLoggerWebMvcConfigurer dewLoggerWebMvcConfigurer;

    @Autowired(required = false)
    private HystrixEventNotifier hystrixEventNotifier;

    @Bean
    @LoadBalanced
    protected RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @PostConstruct
    public void init() {
        if (!devxCloudConfig.getError().getNotifyEmails().isEmpty() && hystrixEventNotifier != null) {
            logger.info("Enabled Failure Event Notifier");
            HystrixPlugins.getInstance().registerEventNotifier(hystrixEventNotifier);
        }
        if (devxCloudConfig.getTraceLog().isEnabled()) {
            logger.info("Enabled Trace Log");
            restTemplate.getInterceptors().add(new DevxTraceRestTemplateInterceptor());
            ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(DevxTraceLogWrap.class);
            root.setLevel(Level.TRACE);
        }
    }

}