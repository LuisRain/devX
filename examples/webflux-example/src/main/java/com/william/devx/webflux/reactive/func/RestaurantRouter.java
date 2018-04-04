package com.william.devx.webflux.reactive.func;

import com.william.devx.webflux.reactive.func.handler.RestaurantHandler;
import io.swagger.models.HttpMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.ipc.netty.http.server.HttpServer;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RouterFunctions.toHttpHandler;

/**
 * Created by sungang on 2018/4/4.
 */
@Configuration
public class RestaurantRouter {


    @Autowired
    private RestaurantHandler restaurantHandler;


    /**
     * 注册自定义RouterFunction
     * RoouterFunction 类似 Spring Web MVC 的 @RequestMapping
     * 用来定义路由信息，每个路由会映射到一个处理方法，当接受 HTTP 请求时候会调用该处理方法。
     */
    @Bean
    public RouterFunction<ServerResponse> restaurantRouter() {
        RouterFunction<ServerResponse> router = route(GET("/reactive/restaurants").and(accept(APPLICATION_JSON_UTF8)), restaurantHandler::findAll)
                .andRoute(GET("/reactive/delay/restaurants").and(accept(APPLICATION_JSON_UTF8)), restaurantHandler::findAllDelay)
                .andRoute(GET("/reactive/restaurants/{id}").and(accept(APPLICATION_JSON_UTF8)), restaurantHandler::get)
                .andRoute(POST("/reactive/restaurants").and(accept(APPLICATION_JSON_UTF8)).and(contentType(APPLICATION_JSON_UTF8)), restaurantHandler::create)
                .andRoute(DELETE("/reactive/restaurants/{id}").and(accept(APPLICATION_JSON_UTF8)), restaurantHandler::delete)
                // 注册自定义HandlerFilterFunction
                .filter((request, next) -> {
                    if (HttpMethod.PUT.equals(request.method())) {
                        return ServerResponse.status(HttpStatus.BAD_REQUEST).build();
                    }
                    return next.handle(request);
                });
        return router;
    }


}
