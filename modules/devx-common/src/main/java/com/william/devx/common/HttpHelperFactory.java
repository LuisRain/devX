package com.william.devx.common;


public class HttpHelperFactory {

    static HttpHelper choose() {
        return choose(200, 20,-1,-1,true);
    }

    /**
     * @param maxTotal    整个连接池最大连接数
     * @param maxPerRoute 每个路由（域）的默认最大连接
     * @param defaultConnectTimeoutMS 默认连接超时时间
     * @param defaultSocketTimeoutMS 默认读取超时时间
     * @param retryAble 是否重试
     */
    static HttpHelper choose(int maxTotal, int maxPerRoute,int defaultConnectTimeoutMS,int defaultSocketTimeoutMS,boolean retryAble) {
        if (DependencyHelper.hasDependency("org.apache.http.impl.client.CloseableHttpClient")) {
            return new ApacheHttpHelper(maxTotal, maxPerRoute,defaultConnectTimeoutMS,defaultSocketTimeoutMS,retryAble);
        }
        return null;
    }

}
