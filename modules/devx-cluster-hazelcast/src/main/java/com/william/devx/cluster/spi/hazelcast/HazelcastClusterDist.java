package com.william.devx.cluster.spi.hazelcast;

import com.william.devx.core.cluster.ClusterDist;
import com.william.devx.core.cluster.ClusterDistLock;
import com.william.devx.core.cluster.ClusterDistMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnBean(HazelcastAdapter.class)
public class HazelcastClusterDist implements ClusterDist {

    @Autowired
    private HazelcastAdapter hazelcastAdapter;

    @Override
    public ClusterDistLock lock(String key) {
        return new HazelcastClusterDistLock(key, hazelcastAdapter.getHazelcastInstance());
    }

    @Override
    public <M> ClusterDistMap<M> map(String key, Class<M> clazz) {
        return new HazelcastClusterDistMap<>(key, hazelcastAdapter.getHazelcastInstance());
    }

}
