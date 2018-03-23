package com.william.devx.core.cluster.spi.redis;

import com.william.devx.core.cluster.Cluster;
import com.william.devx.core.cluster.ClusterDistLock;
import com.william.devx.core.cluster.VoidProcessFun;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.JedisCommands;


public class RedisClusterDistLock implements ClusterDistLock {

    private String key;
    private StringRedisTemplate redisTemplate;

    RedisClusterDistLock(String key, StringRedisTemplate redisTemplate) {
        this.key = "devx:dist:lock:" + key;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void lockWithFun(VoidProcessFun fun) throws Exception {
        try {
            lock();
            fun.exec();
        } finally {
            unLock();
        }
    }

    @Override
    public void tryLockWithFun(VoidProcessFun fun) throws Exception {
        tryLockWithFun(0, fun);
    }

    @Override
    public void tryLockWithFun(long waitMillSec, VoidProcessFun fun) throws Exception {
        if (tryLock(waitMillSec)) {
            try {
                fun.exec();
            } finally {
                unLock();
            }
        }
    }

    @Override
    public void tryLockWithFun(long waitMillSec, long leaseMillSec, VoidProcessFun fun) throws Exception {
        if (tryLock(waitMillSec, leaseMillSec)) {
            try {
                fun.exec();
            } finally {
                unLock();
            }
        }
    }

    @Override
    public void lock() {
        redisTemplate.opsForValue().setIfAbsent(key, getCurrThreadId());
    }

    @Override
    public boolean tryLock() {
        return redisTemplate.opsForValue().setIfAbsent(key, getCurrThreadId()) || redisTemplate.opsForValue().get(key).equals(getCurrThreadId());
    }

    @Override
    public boolean tryLock(long waitMillSec) throws InterruptedException {
        long now = System.currentTimeMillis();
        while (System.currentTimeMillis() - now < waitMillSec) {
            if (isLocked()) {
                Thread.sleep(100);
            } else {
                if (tryLock()) {
                    return Boolean.TRUE;
                }
            }

        }
        return tryLock();
    }

    @Override
    public boolean tryLock(long waitMillSec, long leaseMillSec) throws InterruptedException {
        if (waitMillSec == 0 && leaseMillSec == 0) {
            return tryLock();
        } else if (leaseMillSec == 0) {
            return tryLock(waitMillSec);
        } else if (waitMillSec == 0) {
            return putLockKey(leaseMillSec);
        } else {
            long now = System.currentTimeMillis();
            while (System.currentTimeMillis() - now < waitMillSec) {
                if (isLocked()) {
                    Thread.sleep(100);
                } else if (putLockKey(leaseMillSec)) {
                    return Boolean.TRUE;
                }
            }
            return putLockKey(leaseMillSec);
        }
    }

    @Override
    public boolean unLock() {
        if (getCurrThreadId().equals(redisTemplate.opsForValue().get(key))) {
            redisTemplate.delete(key);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isLocked() {
        return redisTemplate.hasKey(key);
    }

    @Override
    public void delete() {
        redisTemplate.delete(key);
    }

    private boolean putLockKey(long leaseMillSec) {
        RedisConnection redisConnection = redisTemplate.getConnectionFactory().getConnection();
        JedisCommands commands = (JedisCommands) redisConnection.getNativeConnection();
        String res = commands.set(key, getCurrThreadId(), "NX", "PX", leaseMillSec);
        redisConnection.close();
        return (res != null && "OK".equalsIgnoreCase(res)) || redisTemplate.opsForValue().get(key).equals(getCurrThreadId());
    }

    private String getCurrThreadId() {
        return Cluster.CLASS_LOAD_UNIQUE_FLAG + "-" + Thread.currentThread().getId();
    }
}
