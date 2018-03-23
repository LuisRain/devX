package com.william.devx.core.interceptor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
 * Created by sungang on 2017/11/3.
 */
@Configuration
@ConditionalOnWebApplication
public class DevxConfigurer extends WebMvcConfigurerAdapter {

    /**
     * 注册拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new DevxHandlerInterceptor())
                .excludePathPatterns("*.html")
                .excludePathPatterns("*.js")
                .excludePathPatterns("*.css")
                .excludePathPatterns("/error/**")
                .excludePathPatterns("/**/login")
                .excludePathPatterns("/**/system/**");
    }


    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseTrailingSlashMatch(true);
    }
}
