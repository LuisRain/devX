package com.william.devx.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * Created by sungang on 2018/3/16.
 */
@Configuration
@Slf4j
public class DevxTestAutoConfiguration {

    private RedisServer redisServer;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @PostConstruct
    public void init() throws IOException {
        log.info("======================= Enabled Devx Test =======================");
        redisServer = new RedisServer();
        if (!redisServer.isActive()) {
            try {
                redisServer.start();
                redisTemplate.getConnectionFactory().getConnection();
            } catch (Exception e) {
                log.error("Start embedded redis error.", e);
            }
        }
    }

    @PreDestroy
    public void destroy() {
        if (redisServer.isActive()) {
            redisServer.stop();
        }
    }
}
