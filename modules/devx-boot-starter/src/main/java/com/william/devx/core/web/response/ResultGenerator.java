package com.william.devx.core.web.response;


import com.william.devx.core.enums.GlobalErrorInfoEnum;
import com.william.devx.core.utils.MessageUtils;
import com.william.devx.core.enums.ErrorInfo;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by sungang on 2017/8/19.
 */
public class ResultGenerator {


    private static final String DEFAULT_SUCCESS_MESSAGE = "success";

    public static ResultBody genSuccessResult() {
        return new ResultBody()
                .setCode(GlobalErrorInfoEnum.SUCCESS.getCode())
                .setMessage(DEFAULT_SUCCESS_MESSAGE);
    }

    public static ResultBody genSuccessResult(Object data) {
        return new ResultBody()
                .setCode(GlobalErrorInfoEnum.SUCCESS.getCode())
                .setMessage(DEFAULT_SUCCESS_MESSAGE)
                .setResult(data);
    }

    public static ResultBody genFailResult(ErrorInfo errorInfo, Object... agrs) {
        return new ResultBody()
                .setCode(errorInfo.getCode())
                .setMessage(getMessage(errorInfo, agrs));
    }


    public static ResultBody genFailResult(String code, String message) {
        return new ResultBody()
                .setCode(code)
                .setMessage(message);
    }

    private static String getMessage(ErrorInfo errorInfo, Object... agrs) {
        String msg = errorInfo.getMessage();
        if (!StringUtils.isEmpty(errorInfo.getCode()) && null != agrs) {
            msg = MessageUtils.message(errorInfo.getCode(), agrs);
        } else {
            msg = MessageUtils.message(errorInfo.getCode());
        }

        return msg;
    }
}
