package com.william.devx.controller;

import com.william.devx.core.validation.IdNumber;
import com.william.devx.core.validation.CreateGroup;
import com.william.devx.core.validation.Phone;
import com.william.devx.core.validation.UpdateGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by sungang on 2018/3/16.
 */
@RestController
@Api(value = "Web Example", tags = "Web Example", description = "Web Example")
@Validated // URL 类型的验证需要使用此注解
public class WebExampleController {

    /**
     * 最基础的Controller示例
     */
    @GetMapping("example")
    @ApiOperation(value = "示例方式")
    public String example() {
        return "enjoy!";
    }

    /**
     * 数据验证示例，针对 CreateGroup 这一标识组的 bean认证
     */
    @PostMapping(value = "valid-create")
    public User validCreate(@Validated(CreateGroup.class) @RequestBody User user) {
        return user;
    }

    /**
     * 数据验证示例，针对 UpdateGroup 这一标识组的 bean认证，传入的是表单形式
     */
    @PutMapping(value = "valid-update")
    public String validUpdate(@Validated(UpdateGroup.class) User user) {
        return "";
    }

    /**
     * 数据验证示例，URL认证
     */
    @GetMapping(value = "valid-method/{age}")
    public String validInMethod(@Min(value = 2, message = "age必须大于2") @PathVariable("age") int age) {
        return "";
    }


    public static class User {

        // 仅在CreateGroup组下才校验
        @NotNull(groups = CreateGroup.class)
        @IdNumber(message = "身份证号错误", groups = CreateGroup.class)
        private String idCard;
        // CreateGroup、UpdateGroup组下校验
        @Min(value = 10, groups = {CreateGroup.class})
        private int age;
        // CreateGroup、UpdateGroup组下校验
        @Phone(message = "手机号错误", groups = {CreateGroup.class, UpdateGroup.class})
        private String phone;

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }


}
