package com.william.devx.core;

import com.william.devx.Devx;
import com.william.devx.common.$;
import com.william.devx.core.dto.OptInfo;

import java.util.Optional;

/**
 * Dew 上下文处理
 */
public class DevxContext {

    private static final ThreadLocal<DevxContext> CONTEXT = new ThreadLocal<>();

    private static Class optInfoClazz = OptInfo.class;

    /**
     * 当次请求的ID
     */
    private String id;
    /**
     * 请求来源IP
     */
    private String sourceIP;
    /**
     * 请求最初的URL
     */
    private String requestUri;
    /**
     * 请求对应的token
     */
    private String token;

    private Optional innerOptInfo = Optional.empty();

    public static <E extends OptInfo> Class<E> getOptInfoClazz() {
        return optInfoClazz;
    }

    /**
     * 设置自定义的OptInfo
     *
     * @param optInfoClazz
     */
    public static <E extends OptInfo> void setOptInfoClazz(Class<E> optInfoClazz) {
        DevxContext.optInfoClazz = optInfoClazz;
    }

    public <E extends OptInfo> Optional<E> optInfo() {
        if (innerOptInfo.isPresent()) {
            return innerOptInfo;
        }
        if (token != null && !token.isEmpty()) {
            innerOptInfo = Devx.auth.getOptInfo(token);
        } else {
            innerOptInfo = Optional.empty();
        }
        return innerOptInfo;
    }

    public static DevxContext getContext() {
        DevxContext cxt = CONTEXT.get();
        if (cxt == null) {
            cxt = new DevxContext();
            cxt.id = $.field.createUUID();
            cxt.sourceIP = Devx.Info.ip;
            cxt.requestUri = "";
            cxt.token = "";
            setContext(cxt);
        }
        return cxt;
    }

    public static boolean exist(){
        return CONTEXT.get()!=null;
    }

    public static void setContext(DevxContext _context) {
        if (_context.token == null) {
            _context.token = "";
        }
        CONTEXT.set(_context);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSourceIP() {
        return sourceIP;
    }

    public void setSourceIP(String sourceIP) {
        this.sourceIP = sourceIP;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public void setInnerOptInfo(Optional innerOptInfo) {
        this.innerOptInfo = innerOptInfo;
    }

}
