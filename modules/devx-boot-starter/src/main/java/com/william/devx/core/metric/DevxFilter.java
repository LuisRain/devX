package com.william.devx.core.metric;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.ConcurrentReferenceHashMap;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

//@Component
@ConditionalOnClass(Filter.class)
@ConditionalOnProperty(prefix = "devx.metric", name = "enabled", havingValue = "true", matchIfMissing = true)
@Slf4j
public class DevxFilter implements Filter {

    // url->(timestamp,resTime)
    public static final Map<String, LinkedHashMap<Long, Integer>> RECORD_MAP = new ConcurrentReferenceHashMap<>(50, ConcurrentReferenceHashMap.ReferenceType.SOFT);

    private static final String MATCHING_PATTERN_KEY = "org.springframework.web.servlet.HandlerMapping.bestMatchingPattern";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("devxFilter Started");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        long start = Instant.now().toEpochMilli();
        servletRequest.setAttribute("devx.metric.start", start);
        filterChain.doFilter(servletRequest, servletResponse);
        int resTime = (int) (Instant.now().toEpochMilli() - start);
        String method = httpServletRequest.getMethod();
        String matchingPattern = (String) httpServletRequest.getAttribute(MATCHING_PATTERN_KEY);
        if (matchingPattern != null && !matchingPattern.endsWith("/favicon.ico")) {
            String key = "{[" + method + "]:" + matchingPattern + "}";
            if (RECORD_MAP.containsKey(key)) {
                RECORD_MAP.get(key).put(start, resTime);
            } else {
                RECORD_MAP.put(key, new LinkedHashMap<Long, Integer>() {{
                    put(start, resTime);
                }});
            }
        }
    }

    @Override
    public void destroy() {
        log.info("devxFilter destroyed");
    }
}
