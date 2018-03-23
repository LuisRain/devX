package com.william.devx.core.exception.handler;

import com.william.devx.common.Resp;
import com.william.devx.common.StandardCode;
import com.william.devx.core.enums.ErrorInfo;
import com.william.devx.core.enums.GlobalErrorInfoEnum;
import com.william.devx.core.exception.GlobalErrorInfoException;
import com.william.devx.core.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

/**
 * 统一错误码异常处理
 * <p>
 * Created by sungang on 2017/5/19.
 */
@Slf4j
@RestControllerAdvice
public class GlobalErrorInfoHandler {


    @Value("${show-exception}")
    private Boolean showException = false;

    private static Logger logger = LoggerFactory.getLogger(GlobalErrorInfoHandler.class);

    /**
     * 全局系统异常
     *
     * @param request
     * @param exception
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = RuntimeException.class)
    public Resp errorHandlerOverJson(HttpServletRequest request, RuntimeException exception) {
        logger.error("全局异常:{}", exception.getMessage());
        if (showException) {
            exception.printStackTrace();
        }
        return Resp.serverError(GlobalErrorInfoEnum.INTERNAL_SERVER_ERROR.getMessage());
    }

    /**
     * 认证异常
     * @param request
     * @param exception
     * @return
     */
//    @ResponseStatus(HttpStatus.OK)
//    @ExceptionHandler(value = AuthenticationException.class)
//    public ResultBody errorAuthenticationException(HttpServletRequest request, AuthenticationException exception){
//        logger.error("认证失败:{}", exception.getMessage());
//        if (showException){
//            exception.printStackTrace();
//        }
//        return ResultGenerator.genFailResult(GlobalErrorInfoEnum.INTERNAL_SERVER_ERROR.getCode(),exception.getMessage());
//    }
//
//
//    @ResponseStatus(HttpStatus.OK)
//    @ExceptionHandler(value = DataFilterException.class)
//    public ResultBody errorDataFilterException(HttpServletRequest request, DataFilterException exception){
//        logger.error("数据权限过滤失败:{}", exception.getMessage());
//        if (showException){
//            exception.printStackTrace();
//        }
//        return ResultGenerator.genFailResult(GlobalErrorInfoEnum.INTERNAL_SERVER_ERROR.getCode(),exception.getMessage());
//    }


    /**
     * 配置对象参数注解解析失败
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Resp handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.error("参数验证失败", e);
        if (showException) {
            e.printStackTrace();
        }
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> errors = bindingResult.getFieldErrors();
        StringBuffer sb = new StringBuffer();
        for (FieldError error : errors) {
            String field = error.getField();
            String code = error.getDefaultMessage();
            String message = String.format("%s:%s", field, code);
            sb.append(message).append(",");
        }
        return Resp.customFail(StandardCode.BAD_REQUEST.toString(),sb.toString());
    }


    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ConstraintViolationException.class)
    public Resp handleConstraintViolationException(ConstraintViolationException e) {
        logger.error("参数验证失败", e);
        if (showException) {
            e.printStackTrace();
        }
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuffer sb = new StringBuffer();
        for (ConstraintViolation error : violations) {
            String code = error.getMessage();
            String message = String.format("%s", code);
            sb.append(message).append(",");
        }
        return Resp.customFail(StandardCode.BAD_REQUEST.toString(),sb.toString());
    }


    /**
     * GlobalErrorInfoException 系统异常
     *
     * @param request
     * @param exception
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = GlobalErrorInfoException.class)
    public Resp handleGlobalErrorInfoException(HttpServletRequest request, GlobalErrorInfoException exception) {
        logger.error("GlobalErrorInfoException 错误消息:{}", exception.getMessage());
        if (showException) {
            exception.printStackTrace();
        }
        ErrorInfo errorInfo = exception.getErrorInfo();
        return Resp.serverError(errorInfo.getMessage());
    }

    private void getMessage(ErrorInfo errorInfo, Object... agrs) {
        String message = null;
        if (!StringUtils.isEmpty(errorInfo.getCode())) {
            message = MessageUtils.message(errorInfo.getCode(), agrs);
        }
        if (message == null) {
            message = errorInfo.getMessage();
        }
        errorInfo.setMessage(message);
    }
}