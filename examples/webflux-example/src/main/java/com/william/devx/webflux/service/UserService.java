package com.william.devx.webflux.service;

import com.william.devx.mybatis.service.BaseService;
import com.william.devx.webflux.mapper.UserMapper;
import com.william.devx.webflux.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by sungang on 2018/3/22.
 */
@Service
public class UserService extends BaseService<User>{

    @Autowired
    private UserMapper userMapper ;

}
