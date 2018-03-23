package com.william.devx.core.metric;

import com.william.devx.Devx;
import com.william.devx.core.DevxConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.Iterator;
import java.util.Map;

//@Component
@ConditionalOnBean(DevxFilter.class)
public class DevxInitiator {

    @Autowired
    private DevxConfig devxConfig;

    @Autowired
    private DevxFilter devxFilter;

    @Bean
    public FilterRegistrationBean testFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(devxFilter);
        registration.addUrlPatterns("/*");
        registration.setName("devxFilter");
        registration.setOrder(1);
        return registration;
    }

    @PostConstruct
    public void init() {
        long standardTime = Instant.now().minusSeconds(devxConfig.getMetric().getPeriodSec()).toEpochMilli();
        Devx.Timer.periodic(60, () -> {
            for (Map<Long, Integer> map : DevxFilter.RECORD_MAP.values()) {
                Iterator<Map.Entry<Long, Integer>> iterator = map.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<Long, Integer> entry = iterator.next();
                    if (entry.getKey() < standardTime) {
                        iterator.remove();
                    } else {
                        break;
                    }
                }
            }
        });
    }
}
