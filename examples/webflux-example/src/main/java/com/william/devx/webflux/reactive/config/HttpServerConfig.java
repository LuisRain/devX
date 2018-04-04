package com.william.devx.webflux.reactive.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RouterFunction;
import reactor.ipc.netty.http.server.HttpServer;

import static org.springframework.web.reactive.function.server.RouterFunctions.toHttpHandler;

/**
 * Created by sungang on 2018/4/4.
 * 创建 HttpServerConfig 自定义 Http Server，这里创建一个 Netty HTTP 服务器：
 * <p>
 * 推荐 Netty 来运行 Reactive 应用，因为 Netty 是基于异步和事件驱动的。
 */
@Configuration
public class HttpServerConfig {

    @Autowired
    private Environment environment;


    @Bean
    public HttpServer httpServer(RouterFunction<?> routerFunction) {
        // 转化为通用的Reactive HttpHandler
        HttpHandler httpHandler = toHttpHandler(routerFunction);
        // 适配成Netty Server所需的Handler
        ReactorHttpHandlerAdapter httpAdapter = new ReactorHttpHandlerAdapter(httpHandler);
        // 创建Netty Server
        HttpServer server = HttpServer.create("localhost", Integer.valueOf(environment.getProperty("server.port")));
        // 注册Handler并启动Netty Server
        server.newHandler(httpAdapter).block();

        return server;
    }
}
