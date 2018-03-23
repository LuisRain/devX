package com.william.devx.core.enums;

/**
 * 应用系统级别的错误码
 * <p>
 * Created by sungang on 2017/5/19.
 */
public enum GlobalErrorInfoEnum implements ErrorInfo {
    SUCCESS("200","success"),//成功
    FAIL("400","fail"),//失败
    UNAUTHORIZED("401","UNAUTHORIZED"),//未认证（签名错误）
    NOT_FOUND("404","NOT_FOUND"),//接口不存在
    INTERNAL_SERVER_ERROR("500","服务器内部异常!"),//服务器内部错误


    SQL_FILTER("500","包含非法字符!");//服务器内部错误

    private String code;

    private String message;

    GlobalErrorInfoEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }
}
