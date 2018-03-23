package com.william.devx.auth.controller;

import com.william.devx.Devx;
import com.william.devx.auth.dto.LoginDTO;
import com.william.devx.auth.dto.OptInfoExt;
import com.william.devx.auth.dto.User;
import com.william.devx.common.$;
import com.william.devx.common.Resp;
import com.william.devx.core.DevxContext;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class AuthExampleController {

    private static Map<String, User> MOCK_USER_CONTAINER = new HashMap<>();

    @PostConstruct
    public void init() {
        DevxContext.setOptInfoClazz(OptInfoExt.class);
    }

    /**
     * 模拟用户注册
     */
    @PostMapping(value = "user/register")
    public Resp<Void> register(@RequestBody User user) {
        // 实际注册处理
        user.setId($.field.createUUID());
        MOCK_USER_CONTAINER.put(user.getId(), user);
        return Resp.success(null);
    }

    /**
     * 模拟用户登录
     */
    @PostMapping(value = "auth/login")
    public Resp<String> login(@RequestBody LoginDTO loginDTO) {
        // 实际登录处理
        User user = MOCK_USER_CONTAINER.values().stream().filter(u -> u.getIdCard().equals(loginDTO.getIdCard())).findFirst().get();
        String token = $.field.createUUID();
        Devx.auth.setOptInfo(new OptInfoExt()
                .setIdCard(user.getIdCard())
                .setAccountCode($.field.createShortUUID())
                .setToken(token)
                .setName(user.getName())
                .setMobile(user.getPhone()));
        return Resp.success(token);
    }

    /**
     * 模拟业务操作
     */
    @GetMapping(value = "business/someopt")
    public Resp<Void> someOpt() {
        // 获取登录用户信息
        Optional<OptInfoExt> optInfoExtOpt = Devx.auth.getOptInfo();
        if (!optInfoExtOpt.isPresent()) {
            return Resp.unAuthorized("用户认证错误");
        }
        // 登录用户的信息
            optInfoExtOpt.get();
        return Resp.success(null);
    }

    /**
     * 模拟用户注销
     */
    @DeleteMapping(value = "auth/logout")
    public Resp<Void> logout() {
        // 实际注册处理
        Devx.auth.removeOptInfo();
        return Resp.success(null);
    }

}
