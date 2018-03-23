package com.william.devx.idempotent;

import com.william.devx.core.annotation.DevxLoadImmediately;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@DevxLoadImmediately
public class DevxIdempotentAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(DevxIdempotentAutoConfiguration.class);

    @Autowired
    private DevxIdempotentConfig dewIdempotentConfig;
    @Autowired
    private DevxIdempotent dewIdempotent;

    @PostConstruct
    private void init() {
        logger.info("Enabled Dew Idempotent");
    }

}