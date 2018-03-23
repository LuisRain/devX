package com.william.devx;


import com.william.devx.cluster.spi.rabbitmq.RabbitClusterMQ;
import com.william.devx.common.$;
import com.william.devx.core.cluster.ClusterDistLock;
import com.william.devx.core.cluster.ClusterDistMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;

@Component
public class ClusterExampleInitiator {

    private static final Logger logger = LoggerFactory.getLogger(ClusterExampleInitiator.class);

    @PostConstruct
    public void init() throws Exception {
        // cache
        Devx.cluster.cache.flushdb();
        Devx.cluster.cache.del("n_test");
        assert !Devx.cluster.cache.exists("n_test");
        Devx.cluster.cache.setex("n_test", "{\"name\":\"jzy\"}", 1);
        assert Devx.cluster.cache.exists("n_test");
        assert "jzy".equals($.json.toJson(Devx.cluster.cache.get("n_test")).get("name").asText());
        Thread.sleep(1000);
        assert !Devx.cluster.cache.exists("n_test");
        assert null == Devx.cluster.cache.get("n_test");
        // ...

        // dist map
        ClusterDistMap<TestMapObj> mapObj = Devx.cluster.dist.map("test_obj_map", TestMapObj.class);
        mapObj.clear();
        TestMapObj obj = new TestMapObj();
        obj.a = "测试";
        mapObj.put("test", obj);
        assert "测试".equals(mapObj.get("test").a);
        // ...

        // dist lock
        ClusterDistLock lock = Devx.cluster.dist.lock("test_lock");
        // tryLock 示例，等待0ms，忘了手工unLock或出异常时1s后自动解锁
        if (lock.tryLock(0, 1000)) {
            try {
                // 已加锁，执行业务方法
            } finally {
                // 必须手工解锁
                lock.unLock();
            }
        }
        // tryLockWithFun 示例
        lock.tryLockWithFun(0, 1000, () -> {
            // 已加锁，执行业务方法，tryLockWithFun会将业务方法包裹在try-cache中，无需手工解锁
        });

        // pub-sub
        Devx.cluster.mq.subscribe("test_pub_sub", message -> logger.info("pub_sub>>" + message));
        Thread.sleep(1000);
        Devx.cluster.mq.publish("test_pub_sub", "msgA");
        Devx.cluster.mq.publish("test_pub_sub", "msgB");
        // req-resp
        Devx.cluster.mq.response("test_rep_resp", message -> logger.info("req_resp>>" + message));
        Devx.cluster.mq.request("test_rep_resp", "msg1");
        Devx.cluster.mq.request("test_rep_resp", "msg2");
        // rabbit confirm
        if (Devx.cluster.mq instanceof RabbitClusterMQ) {
            boolean success = ((RabbitClusterMQ) Devx.cluster.mq).publish("test_pub_sub", "confirm message", true);
            success = ((RabbitClusterMQ) Devx.cluster.mq).request("test_rep_resp", "confirm message", true);
        }
    }

    static class TestMapObj implements Serializable {
        private String a;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }
    }

}
