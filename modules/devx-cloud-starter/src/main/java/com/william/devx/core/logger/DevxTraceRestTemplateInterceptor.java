package com.william.devx.core.logger;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class DevxTraceRestTemplateInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        DevxTraceLogWrap.request("RestTemplate", request.getMethod().name(), request.getURI().toURL().toString());
        ClientHttpResponse response = execution.execute(request, body);
        DevxTraceLogWrap.response("RestTemplate", response.getRawStatusCode(), request.getMethod().name(), request.getURI().toURL().toString());
        return response;
    }

}
