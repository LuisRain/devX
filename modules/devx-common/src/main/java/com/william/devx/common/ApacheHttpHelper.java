package com.william.devx.common;

import com.alibaba.fastjson.JSON;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.SocketException;
import java.net.URLDecoder;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

import static org.joox.JOOX.$;

/**
 * HTTP操作
 */
public class ApacheHttpHelper implements HttpHelper {

    private static final Logger logger = LoggerFactory.getLogger(ApacheHttpHelper.class);

    private CloseableHttpClient httpClient = null;
    private boolean retryAble = true;
    private int defaultConnectTimeoutMS = -1;
    private int defaultSocketTimeoutMS = -1;

    /**
     * @param maxTotal                整个连接池最大连接数
     * @param maxPerRoute             每个路由（域）的默认最大连接
     * @param defaultConnectTimeoutMS 默认连接超时时间
     * @param defaultSocketTimeoutMS  默认读取超时时间
     * @param retryAble               是否重试
     */
    ApacheHttpHelper(int maxTotal, int maxPerRoute, int defaultConnectTimeoutMS, int defaultSocketTimeoutMS, boolean retryAble) {
        this.defaultConnectTimeoutMS = defaultConnectTimeoutMS;
        this.defaultSocketTimeoutMS = defaultSocketTimeoutMS;
        this.retryAble = retryAble;
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        SSLContext sslContext = null;
        try {
            sslContext = new SSLContextBuilder().loadTrustMaterial(null, (arg0, arg1) -> true).build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            e.printStackTrace();
        }
        httpClientBuilder.setSSLContext(sslContext);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE)).build();
        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connMgr.setMaxTotal(maxTotal);
        connMgr.setDefaultMaxPerRoute(maxPerRoute);
        httpClientBuilder.setConnectionManager(connMgr);
        httpClient = httpClientBuilder.build();
    }

    @Override
    public String get(String url) throws IOException {
        return get(url, null, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public String get(String url, Map<String, String> header) throws IOException {
        return get(url, header, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public String get(String url, String contentType) throws IOException {
        return get(url, null, contentType, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public String get(String url, Map<String, String> header, String contentType, String charset, int connectTimeoutMS, int socketTimeoutMS) throws IOException {
        return request("GET", url, null, header, contentType, charset, connectTimeoutMS, socketTimeoutMS).result;
    }

    @Override
    public ResponseWrap getWrap(String url) throws IOException {
        return getWrap(url, null, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public ResponseWrap getWrap(String url, Map<String, String> header) throws IOException {
        return getWrap(url, header, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public ResponseWrap getWrap(String url, String contentType) throws IOException {
        return getWrap(url, null, contentType, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public ResponseWrap getWrap(String url, Map<String, String> header, String contentType, String charset, int connectTimeoutMS, int socketTimeoutMS) throws IOException {
        return request("GET", url, null, header, contentType, charset, connectTimeoutMS, socketTimeoutMS);
    }

    @Override
    public String post(String url, Object body) throws IOException {
        return post(url, body, null, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public String post(String url, Object body, Map<String, String> header) throws IOException {
        return post(url, body, header, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public String post(String url, Object body, String contentType) throws IOException {
        return post(url, body, null, contentType, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public String post(String url, Object body, Map<String, String> header, String contentType, String charset, int connectTimeoutMS, int socketTimeoutMS) throws IOException {
        return request("POST", url, body, header, contentType, charset, connectTimeoutMS, socketTimeoutMS).result;
    }

    @Override
    public ResponseWrap postWrap(String url, Object body) throws IOException {
        return postWrap(url, body, null, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public ResponseWrap postWrap(String url, Object body, Map<String, String> header) throws IOException {
        return postWrap(url, body, header, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public ResponseWrap postWrap(String url, Object body, String contentType) throws IOException {
        return postWrap(url, body, null, contentType, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public ResponseWrap postWrap(String url, Object body, Map<String, String> header, String contentType, String charset, int connectTimeoutMS, int socketTimeoutMS) throws IOException {
        return request("POST", url, body, header, contentType, charset, connectTimeoutMS, socketTimeoutMS);
    }

    @Override
    public String put(String url, Object body) throws IOException {
        return put(url, body, null, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public String put(String url, Object body, Map<String, String> header) throws IOException {
        return put(url, body, header, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public String put(String url, Object body, String contentType) throws IOException {
        return put(url, body, null, contentType, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public String put(String url, Object body, Map<String, String> header, String contentType, String charset, int connectTimeoutMS, int socketTimeoutMS) throws IOException {
        return request("PUT", url, body, header, contentType, charset, connectTimeoutMS, socketTimeoutMS).result;
    }

    @Override
    public ResponseWrap putWrap(String url, Object body) throws IOException {
        return putWrap(url, body, null, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public ResponseWrap putWrap(String url, Object body, Map<String, String> header) throws IOException {
        return putWrap(url, body, header, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public ResponseWrap putWrap(String url, Object body, String contentType) throws IOException {
        return putWrap(url, body, null, contentType, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public ResponseWrap putWrap(String url, Object body, Map<String, String> header, String contentType, String charset, int connectTimeoutMS, int socketTimeoutMS) throws IOException {
        return request("PUT", url, body, header, contentType, charset, connectTimeoutMS, socketTimeoutMS);
    }

    @Override
    public String delete(String url) throws IOException {
        return delete(url, null, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public String delete(String url, Map<String, String> header) throws IOException {
        return delete(url, header, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public String delete(String url, String contentType) throws IOException {
        return delete(url, null, contentType, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public String delete(String url, Map<String, String> header, String contentType, String charset, int connectTimeoutMS, int socketTimeoutMS) throws IOException {
        return request("DELETE", url, null, header, contentType, charset, connectTimeoutMS, socketTimeoutMS).result;
    }

    @Override
    public ResponseWrap deleteWrap(String url) throws IOException {
        return deleteWrap(url, null, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public ResponseWrap deleteWrap(String url, Map<String, String> header) throws IOException {
        return deleteWrap(url, header, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public ResponseWrap deleteWrap(String url, String contentType) throws IOException {
        return deleteWrap(url, null, contentType, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public ResponseWrap deleteWrap(String url, Map<String, String> header, String contentType, String charset, int connectTimeoutMS, int socketTimeoutMS) throws IOException {
        return request("DELETE", url, null, header, contentType, charset, connectTimeoutMS, socketTimeoutMS);
    }

    @Override
    public Map<String, String> head(String url) throws IOException {
        return head(url, null, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public Map<String, String> head(String url, Map<String, String> header) throws IOException {
        return head(url, header, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public Map<String, String> head(String url, String contentType) throws IOException {
        return head(url, null, contentType, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public Map<String, String> head(String url, Map<String, String> header, String contentType, String charset, int connectTimeoutMS, int socketTimeoutMS) throws IOException {
        return request("HEAD", url, null, header, contentType, charset, connectTimeoutMS, socketTimeoutMS).head;
    }

    @Override
    public Map<String, String> options(String url) throws IOException {
        return options(url, null, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public Map<String, String> options(String url, Map<String, String> header) throws IOException {
        return options(url, header, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public Map<String, String> options(String url, String contentType) throws IOException {
        return options(url, null, contentType, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public Map<String, String> options(String url, Map<String, String> header, String contentType, String charset, int connectTimeoutMS, int socketTimeoutMS) throws IOException {
        return request("OPTIONS", url, null, header, contentType, charset, connectTimeoutMS, socketTimeoutMS).head;
    }

    @Override
    public ResponseWrap request(String method, String url, Object body, Map<String, String> header, String contentType, String charset, int connectTimeoutMS, int socketTimeoutMS) throws IOException {
        return request(method, url, body, header, contentType, charset, connectTimeoutMS, socketTimeoutMS, 0);
    }

    /**
     * 发起请求
     *
     * @param method           http方法
     * @param url              请求url
     * @param body             请求体，用于post、put
     *                         如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *                         如果content-type是xml时，body只能是Document或Xml的String格式
     *                         如果content-type是multipart/form-data时，body只能是File格式
     *                         其它情况下，body可以是任意格式
     * @param header           请求头
     * @param contentType      content-type
     * @param charset          请求与返回内容编码
     * @param connectTimeoutMS 连接超时时间
     * @param socketTimeoutMS  读取超时时间
     * @param retry            重试次数
     * @return 返回结果
     */
    private ResponseWrap request(String method, String url, Object body, Map<String, String> header, String contentType, String charset, int connectTimeoutMS, int socketTimeoutMS, int retry) throws IOException {
        if (header == null) {
            header = new HashMap<>();
        }
        if (body != null && body instanceof File) {
            contentType = "multipart/form-data";
        } else if (contentType == null) {
            contentType = "application/json; charset=utf-8";
        }
        if (charset == null) {
            charset = "UTF-8";
        }
        HttpRequestBase httpMethod = null;
        switch (method.toUpperCase()) {
            case "GET":
                httpMethod = new HttpGet(url);
                break;
            case "POST":
                httpMethod = new HttpPost(url);
                break;
            case "PUT":
                httpMethod = new HttpPut(url);
                break;
            case "DELETE":
                httpMethod = new HttpDelete(url);
                break;
            case "HEAD":
                httpMethod = new HttpHead(url);
                break;
            case "OPTIONS":
                httpMethod = new HttpOptions(url);
                break;
        }
        httpMethod.setConfig(RequestConfig.custom().setSocketTimeout(socketTimeoutMS).setConnectTimeout(connectTimeoutMS).build());
        for (Map.Entry<String, String> entry : header.entrySet()) {
            httpMethod.addHeader(entry.getKey(), entry.getValue());
        }
        if (contentType != null) {
            httpMethod.addHeader("Content-Type", contentType);
        }
        logger.trace("HTTP [" + method + "]" + url);
        if (body != null) {
            HttpEntity entity;
            switch (contentType.toLowerCase()) {
                case "application/x-www-form-urlencoded":
                    if (body instanceof Map<?, ?>) {
                        List<NameValuePair> m = new ArrayList<>();
                        ((Map<String, String>) body).forEach((key, value) -> m.add(new BasicNameValuePair(key, value)));
                        entity = new UrlEncodedFormEntity(m, charset);
                        break;
                    }
                case "xml":
                    if (body instanceof Document) {
                        entity = new StringEntity($((Document) body).toString(), charset);
                    } else if (body instanceof String) {
                        entity = new StringEntity((String) body, charset);
                    } else {
                        logger.error("Not support return type [" + body.getClass().getName() + "] by xml");
                        entity = new StringEntity("", charset);
                    }
                    break;
                case "multipart/form-data":
                    httpMethod.addHeader("Content-Transfer-Encoding", "binary");
                    entity = MultipartEntityBuilder.create()
                            .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                            .addBinaryBody(((File) body).getName(), (File) body, ContentType.APPLICATION_OCTET_STREAM, ((File) body).getName())
                            .build();
                    // delete custom value,httpclient will use like this "multipart/form-data; boundary=---------------------------7e1295335048a"
                    httpMethod.removeHeaders("Content-Type");
                    break;
                default:
                    if (body instanceof String) {
                        entity = new StringEntity((String) body, charset);
                    } else if (body instanceof Integer || body instanceof Long || body instanceof Float || body instanceof Double || body instanceof BigDecimal || body instanceof Boolean) {
                        entity = new StringEntity(body.toString(), charset);
                    } else if (body instanceof Date) {
                        entity = new StringEntity(((Date) body).getTime() + "", charset);
                    } else {
                        entity = new StringEntity(JSON.toJSONString(body), charset);
                    }
            }
            ((HttpEntityEnclosingRequestBase) httpMethod).setEntity(entity);
        }
        try (CloseableHttpResponse response = httpClient.execute(httpMethod)) {
            ResponseWrap responseWrap = new ResponseWrap();
            if (!(httpMethod instanceof HttpHead || httpMethod instanceof HttpOptions)) {
                responseWrap.result = EntityUtils.toString(response.getEntity(), charset);
            } else {
                responseWrap.result = "";
            }
            responseWrap.statusCode = response.getStatusLine().getStatusCode();
            responseWrap.head = Arrays.stream(response.getAllHeaders()).collect(Collectors.toMap(Header::getName, Header::getValue));
            for (Map.Entry<String, String> entry : responseWrap.head.entrySet()) {
                entry.setValue(URLDecoder.decode(entry.getValue(), charset));
            }
            return responseWrap;
        } catch (SocketException | ConnectTimeoutException | NoHttpResponseException e) {
            // 同络错误重试5次
            if (retryAble && retry <= 5) {
                try {
                    Thread.sleep(1000 * retry);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                logger.warn("HTTP [" + httpMethod.getMethod() + "] " + url + " ERROR. retry " + (retry + 1) + ".");
                return request(method, url, body, header, contentType, charset, connectTimeoutMS, socketTimeoutMS, retry + 1);
            } else {
                logger.warn("HTTP [" + httpMethod.getMethod() + "] " + url + " ERROR. retry " + (retry + 1) + ".");
                throw e;
            }
        } catch (IOException e) {
            logger.warn("HTTP [" + httpMethod.getMethod() + "] " + url + " ERROR. retry " + (retry + 1) + ".");
            throw e;
        }
    }

}
