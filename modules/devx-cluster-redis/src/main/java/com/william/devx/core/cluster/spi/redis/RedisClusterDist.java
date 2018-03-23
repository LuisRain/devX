package com.william.devx.core.cluster.spi.redis;

import com.william.devx.core.cluster.ClusterDistLock;
import com.william.devx.core.cluster.ClusterDistMap;
import com.william.devx.core.cluster.ClusterDist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;


/**
 * Redis 分布式锁
 */
@Component
@ConditionalOnExpression("#{'${devx.cluster.cache}'=='redis' || '${devx.cluster.mq}'=='redis' || '${devx.cluster.dist}'=='redis'}")
public class RedisClusterDist implements ClusterDist {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public ClusterDistLock lock(String key) {
        return new RedisClusterDistLock(key, redisTemplate);
    }

    @Override
    public <M> ClusterDistMap<M> map(String key, Class<M> clazz) {
        return new RedisClusterDistMap<>(key, clazz, redisTemplate);
    }


}
