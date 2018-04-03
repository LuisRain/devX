package com.william.devx.webflux.controller;

import com.william.devx.webflux.model.User;
import com.william.devx.webflux.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sungang on 2018/3/22.
 */
@RestController
@RequestMapping("user")
public class UserController  {


    @Autowired
    private UserService userService;


//    @GetMapping("/tweets")
//    public Flux<User> getAllUser() {
//        return userService.findAll();
//    }


}

