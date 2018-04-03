package com.william.devx.idempotent.interceptor;

import com.william.devx.core.web.controller.ErrorController;
import com.william.devx.idempotent.DevxIdempotent;
import com.william.devx.idempotent.DevxIdempotentConfig;
import com.william.devx.idempotent.annotations.Idempotent;
import com.william.devx.idempotent.strategy.StrategyEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 接口 拦截器
 */
@Component
public class DevxIdempotentHandlerInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private DevxIdempotentConfig DevxIdempotentConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Idempotent idempotent = ((HandlerMethod) handler).getMethod().getAnnotation(Idempotent.class);
        if (idempotent == null) {
            return super.preHandle(request, response, handler);
        }
        // 参数设置
        String optTypeFlag = StringUtils.isEmpty(idempotent.optTypeFlag()) ? DevxIdempotentConfig.getDefaultOptTypeFlag() : idempotent.optTypeFlag();
        String optType = request.getHeader(optTypeFlag);
        if (StringUtils.isEmpty(optType)) {
            optType = request.getParameter(optTypeFlag);
        }
        String optIdFlag = StringUtils.isEmpty(idempotent.optIdFlag()) ? DevxIdempotentConfig.getDefaultOptIdFlag() : idempotent.optIdFlag();
        String optId = request.getHeader(optIdFlag);
        if (StringUtils.isEmpty(optId)) {
            optId = request.getParameter(optIdFlag);
        }
        if (!DevxIdempotent.existOptTypeInfo(optType)) {
            long expireMs = idempotent.expireMs() == -1 ? DevxIdempotentConfig.getDefaultExpireMs() : idempotent.expireMs();
            boolean needConfirm = idempotent.needConfirm();
            StrategyEnum strategy = idempotent.strategy() == StrategyEnum.AUTO ? DevxIdempotentConfig.getDefaultStrategy() : idempotent.strategy();
            DevxIdempotent.initOptTypeInfo(optType, needConfirm, expireMs, strategy);
        }
        switch (DevxIdempotent.process(optType, optId)) {
            case NOT_EXIST:
                return super.preHandle(request, response, handler);
            case UN_CONFIRM:
                ErrorController.error(request, response, 409, "The last operation was still going on, please wait.", DevxIdempotentException.class.getName());
                return false;
            case CONFIRMED:
                ErrorController.error(request, response, 423, "Resources have been processed, can't repeat the request.", DevxIdempotentException.class.getName());
                return false;
            default:
                return false;
        }
    }

}
