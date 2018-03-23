package com.william.devx.core.web.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.william.devx.Devx;
import com.william.devx.common.$;
import com.william.devx.common.Resp;
import com.william.devx.core.DevxConfig;
import com.william.devx.core.metric.DevxFilter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by sungang on 2018/3/16.
 */
@RestController
@RequestMapping("${error.path:/error}")
@ConditionalOnProperty(prefix = "devx.basic.format", name = "useUnityError", havingValue = "true",matchIfMissing = true)
@Slf4j
public class ErrorController extends AbstractErrorController{


    private static final int FALL_BACK_STATUS = 500;
    private static final Pattern MESSAGE_CHECK = Pattern.compile("^\\{\"code\":\"\\w*\",\"message\":\".*\",\"customHttpCode\":.*}$");

    private static final String SPECIAL_ERROR_FLAG = "org.springframework.boot.autoconfigure.web.DefaultErrorAttributes.ERROR";
    private static final String DETAIL_FLAG = " Detail:";

    @Value("${error.path:/error}")
    private String errorPath;

    public ErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    @Override
    public String getErrorPath() {
        return errorPath;
    }


    @RequestMapping()
    @ResponseBody
    public Object error(HttpServletRequest request) {
        Object specialError = request.getAttribute(SPECIAL_ERROR_FLAG);
        if (specialError instanceof Resp.FallbackException) {
            addRequestRecord(request);
            return ResponseEntity.status(FALL_BACK_STATUS).contentType(MediaType.APPLICATION_JSON_UTF8).body(((Resp.FallbackException) specialError).getMessage());
        }
        Map<String, Object> error = getErrorAttributes(request, false);
        String path = (String) error.getOrDefault("path", Devx.context().getRequestUri());
        int statusCode = (int) error.getOrDefault("status", -1);
        String message = error.getOrDefault("message", "").toString();
        String exClass = (String) error.getOrDefault("exception", "");
        String exMsg = (String) error.getOrDefault("error", "");
        List exDetail = null;
        if (error.containsKey("errors") && !((List) error.get("errors")).isEmpty()) {
            exDetail = (List) error.get("errors");
        }
        Object[] result = error(request, path, statusCode, message, exClass, exMsg, exDetail, (Throwable) specialError);
        return ResponseEntity.status((int) result[0]).contentType(MediaType.APPLICATION_JSON_UTF8).body(result[1]);
    }

    public static void error(HttpServletRequest request, HttpServletResponse response, int statusCode, String message, String exClass) throws IOException {
        Object[] confirmedError = error(request, request.getRequestURI(), statusCode, message, exClass, "", null, null);
        response.setStatus((Integer) confirmedError[0]);
        response.setContentType(String.valueOf(MediaType.APPLICATION_JSON_UTF8));
        response.getWriter().write((String) confirmedError[1]);
        response.getWriter().flush();
        response.getWriter().close();
    }

    private static Object[] error(HttpServletRequest request, String path, int statusCode, String message, String exClass, String exMsg, List exDetail, Throwable specialError) {
        String requestFrom = request.getHeader(Devx.Constant.HTTP_REQUEST_FROM_FLAG);
        int httpCode = statusCode;
        String busCode = statusCode + "";
        if (!StringUtils.isEmpty(exClass) && Devx.devxConfig.getBasic().getErrorMapping().containsKey(exClass)) {
            // Found Error Mapping
            DevxConfig.Basic.ErrorMapping errorMapping = Devx.devxConfig.getBasic().getErrorMapping().get(exClass);
            if (!StringUtils.isEmpty(errorMapping.getHttpCode())) {
                httpCode = errorMapping.getHttpCode();
            }
            if (!StringUtils.isEmpty(errorMapping.getBusinessCode())) {
                busCode = errorMapping.getBusinessCode();
            }
            if (!StringUtils.isEmpty(errorMapping.getMessage())) {
                message = errorMapping.getMessage();
            }
        }
        if (MESSAGE_CHECK.matcher(message).matches()) {
            JsonNode detail = $.json.toJson(message);
            busCode = detail.get("code").asText();
            message = detail.get("message").asText();
            if (detail.has("customHttpCode") && detail.get("customHttpCode").asInt() != -1) {
                // 使用自定义http状态码
                httpCode = detail.get("customHttpCode").asInt();
            }
        }
        if (specialError != null) {
            if (specialError instanceof ConstraintViolationException) {
                ArrayNode errorExt = $.json.createArrayNode();
                ((ConstraintViolationException) specialError).getConstraintViolations()
                        .forEach(cv ->
                                errorExt.add($.json.createObjectNode()
                                        .put("field", "")
                                        .put("reason", ((ConstraintDescriptorImpl<?>) cv.getConstraintDescriptor())
                                                .getAnnotationType().getSimpleName())
                                        .put("msg", cv.getMessage()))
                        );
                message += DETAIL_FLAG + $.json.toJsonString(errorExt);
            } else {
                if (exDetail != null && !exDetail.isEmpty()) {
                    ArrayNode errorExt = $.json.createArrayNode();
                    for (JsonNode json : $.json.toJson(exDetail)) {
                        errorExt.add($.json.createObjectNode()
                                .put("field", json.get("field").asText(""))
                                .put("reason", json.get("codes").get(0).asText().split("\\.")[0])
                                .put("msg", json.get("defaultMessage").asText("")));
                    }
                    message += DETAIL_FLAG + $.json.toJsonString(errorExt);
                }
            }
        }
        log.error("Request [{}] from [{}] {} , error {} : {}", path, requestFrom, Devx.context().getSourceIP(), busCode, message);
        String body = "";
        if (!Devx.devxConfig.getBasic().getFormat().isReuseHttpState()) {
            if (specialError instanceof ConstraintViolationException) {
                busCode = "400";
            }
            if (httpCode >= 500 && httpCode < 600) {
                httpCode = FALL_BACK_STATUS;
            } else {
                httpCode = 200;
            }
            Resp resp = Resp.customFail(busCode + "", "[" + exMsg + "]" + message);
            body = $.json.toJsonString(resp);
        } else {
            JsonNode jsonNode = $.json.createObjectNode()
                    .set("error", $.json.createObjectNode()
                            .put(Devx.devxConfig.getBasic().getFormat().getCodeFieldName(), busCode)
                            .put(Devx.devxConfig.getBasic().getFormat().getMessageFieldName(), message)
                    );
            body = jsonNode.toString();
        }
        addRequestRecord(request);
        return new Object[]{httpCode, body};
    }


    private static void addRequestRecord(HttpServletRequest request) {
        Long start = (Long) request.getAttribute("dew.metric.start");
        if (start==null){
            return;
        }
        String key = "{[" + request.getMethod() + "]:/error}";
        int resTime = (int) (Instant.now().toEpochMilli() - start);
        if (DevxFilter.RECORD_MAP.containsKey(key)) {
            DevxFilter.RECORD_MAP.get(key).put(start, resTime);
        } else {
            DevxFilter.RECORD_MAP.put(key, new LinkedHashMap<Long, Integer>() {{
                put(start, resTime);
            }});
        }
    }

}
