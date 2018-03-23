package com.william.devx.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sungang on 2017/11/3.
 */
@Component
@ConfigurationProperties(prefix = "devx")
@Data
public class DevxConfig {
    //基础信息
    private Basic basic = new Basic();
    //集群功能
    private Cluster cluster = new Cluster();
    //安全功能
    private Security security = new Security();
    //指标统计
    private Metric metric = new Metric();


    @Data
    public static class Metric {
        private boolean enabled = true; //是否启用Devx的指标统计
        private long periodSec = 600; //默认600 # 指定统计周期（多少秒内的指标统计）
    }

    @Data
    public static class Basic {

        private String name = ""; //服务名称，用于API文档显示等
        private String version = "1.0"; //服务版本号，用于API文档显示等
        private String desc = ""; //服务描述，用于API文档显示等
        private String webSite = ""; //官网 用于API文档显示等
        /**
         *
         */
        private Doc doc = new Doc();
        private Format format = new Format();
        private Map<String, ErrorMapping> errorMapping = new HashMap<>();

        /**
         * 文档
         */
        @Data
        public static class Doc {
            private boolean enabled = true; //是否启用默认文档配置，关闭后可自定义文档管理
            private String basePackage = ""; //API文档要扫描的根包，多指定到 Controller 包中
        }

        @Data
        public static class Format {
            private boolean useUnityError = true; //是否启用统一响应
            private boolean reuseHttpState = false; //true:重用http状态码，false:使用协议无关格式
            // 兼容原系统设置
            private String messageFieldName = "message";
            private String codeFieldName = "code";
        }

        @Data
        public static class ErrorMapping {
            private int httpCode;//http状态码，不存在时使用实例级http状态码
            private String businessCode;//业务编码，不存在时使用实例级业务编码
            private String message;//错误描述，不存在时使用实例级错误描述
        }

    }

    @Data
    public static class Cluster {
        private String mq = "redis";//MQ实现，可选 redis/hazelcast/rabbit
        private String cache = "redis"; //缓存实现
        private String dist = "redis"; //分布式锁和Map实现，可选 redis/hazelcast
        private String election = "eureka";//领导者选举实现，可选 eureka
    }

    @Data
    public static class Security {
        /**
         * 跨域设置
         */
        private SecurityCORS cors = new SecurityCORS();
        private String tokenFlag = "__devx_token__"; //token 标识
        private boolean tokenInHeader = true; //true：token标识在 header 中，反之在url参数中
        private boolean tokenHash = false; //token值是否需要hash，用于解决token值有特殊字符的情况
        private List<String> includeServices; //服务白名单，要求源服务头部带上 Request-From
        private List<String> excludeServices; //服务黑名单，要求源服务头部带上 Request-From
        private String authAdapter = "basic"; //
    }

    @Data
    public static class SecurityCORS {
        private String allowOrigin = "*";//允许的来源，建议修改
        private String allowMethods = "POST,GET,OPTIONS,PUT,DELETE,HEAD"; //# 允许的方法
        private String allowHeaders = "x-requested-with,content-type";//允许的头信息
    }
}
