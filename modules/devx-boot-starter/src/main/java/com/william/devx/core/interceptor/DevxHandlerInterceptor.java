package com.william.devx.core.interceptor;

import com.william.devx.Devx;
import com.william.devx.common.$;
import com.william.devx.core.DevxContext;
import com.william.devx.core.web.controller.ErrorController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;

/**
 * Devx Servlet拦截器
 * <p>
 * <p>
 * Created by sungang on 2017/11/3.
 */
@Slf4j
public class DevxHandlerInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestFrom = request.getHeader(Devx.Constant.HTTP_REQUEST_FROM_FLAG);
        /**
         * 仅支持白名单内的服务
         */
        if (Devx.devxConfig.getSecurity().getIncludeServices() != null) {
            for (String v : Devx.devxConfig.getSecurity().getIncludeServices()) {
                if (!v.equalsIgnoreCase(requestFrom)) {
                    ErrorController.error(request, response, 401, "The [" + requestFrom + "] does NOT allow access to this service.", AuthException.class.getName());
                    return false;
                }
            }
        }
        /**
         * 排除黑名单中的服务
         */
        if (Devx.devxConfig.getSecurity().getIncludeServices() == null && Devx.devxConfig.getSecurity().getExcludeServices() != null) {
            for (String v : Devx.devxConfig.getSecurity().getExcludeServices()) {
                if (v.equalsIgnoreCase(requestFrom)) {
                    ErrorController.error(request, response, 401, "The [" + requestFrom + "] does NOT allow access to this service.", AuthException.class.getName());
                    return false;
                }
            }
        }
        // 配置跨域参数
        response.addHeader("Access-Control-Allow-Origin", Devx.devxConfig.getSecurity().getCors().getAllowOrigin());
        response.addHeader("Access-Control-Allow-Methods", Devx.devxConfig.getSecurity().getCors().getAllowMethods());
        response.addHeader("Access-Control-Allow-Headers", Devx.devxConfig.getSecurity().getCors().getAllowHeaders());
        response.addHeader("Access-Control-Max-Age", "3600000");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        if (request.getMethod().equalsIgnoreCase("OPTIONS") || request.getMethod().equalsIgnoreCase("HEAD")) {
            return super.preHandle(request, response, handler);
        }

        String token;
        if (Devx.devxConfig.getSecurity().isTokenInHeader()) {
            token = request.getHeader(Devx.devxConfig.getSecurity().getTokenFlag());
        } else {
            token = request.getParameter(Devx.devxConfig.getSecurity().getTokenFlag());
        }
        if (token != null) {
            token = URLDecoder.decode(token, "UTF-8");
            if (Devx.devxConfig.getSecurity().isTokenHash()) {
                token = $.security.digest.digest(token, "MD5");
            }
        }
        DevxContext context = new DevxContext();
        context.setId($.field.createUUID());
        context.setSourceIP(Devx.Util.getRealIP(request));
        context.setRequestUri(request.getRequestURI());
        context.setToken(token);
        DevxContext.setContext(context);

        log.trace("[{}] {}{} from {}", request.getMethod(), request.getRequestURI(), request.getQueryString() == null ? "" : "?" + request.getQueryString(), Devx.context().getSourceIP());
        return super.preHandle(request, response, handler);
    }


}
