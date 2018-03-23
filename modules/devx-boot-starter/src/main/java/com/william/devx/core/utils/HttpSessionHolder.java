package com.william.devx.core.utils;

import javax.servlet.http.HttpSession;

/**
 *
 * 非Controller中获取当前session的工具类
 *
 * Created by sungang on 2017/12/20.
 */
public class HttpSessionHolder {

    private static ThreadLocal<HttpSession> tl = new ThreadLocal<HttpSession>();

    public static void put(HttpSession s) {
        tl.set(s);
    }

    public static HttpSession get() {
        return tl.get();
    }

    public static void remove() {
        tl.remove();
    }
}
