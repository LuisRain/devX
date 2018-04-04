package com.william.devx.webflux.controller;

import com.william.devx.webflux.model.User;
import com.william.devx.webflux.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * Created by sungang on 2018/3/22.
 */
@RestController
@RequestMapping("api/user")
@Api(value = "WebFlux Example", tags = "WebFlux Example", description = "WebFlux Example")
public class UserController {


    @Autowired
    private UserService userService;

    /**
     * 查询用户列表
     *
     * @return
     */
    @GetMapping("")
    @ApiOperation(value = "查询用户列表")
    public Flux<User> getAllUser() {

//        Flux<User> users = Flux.create(userFluxSink -> {
//            userService.selectAll().forEach(user -> {
//                userFluxSink.next(user);
//            });
//            userFluxSink.complete();
//        });

        Flux<User> users = Flux.fromIterable(userService.selectAll());
        return users;
    }

    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    @PostMapping
    @ApiOperation(value = "添加用户")
    public Mono<Integer> add(@RequestBody User user) {
//        Mono<Integer> id = Mono.create(userMonoSink -> userMonoSink.success(userService.insert(user)));

        Mono<Integer> id = Mono.justOrEmpty(userService.insert(user));
        return id;
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @PostMapping("{id}")
    @ApiOperation(value = "删除用户")
    public Mono<Integer> del(@PathVariable Long id) {
//        Mono<Integer> r = Mono.create(userMonoSink -> userMonoSink.success(userService.deleteByPrimaryKey(id)));
        Mono<Integer> r = Mono.justOrEmpty(userService.deleteByPrimaryKey(id));
        return r;
    }
}

