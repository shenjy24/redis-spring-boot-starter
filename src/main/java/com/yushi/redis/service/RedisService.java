package com.yushi.redis.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 【 Redis服务类 】
 *
 * @author shenjy 2018/11/19
 */
@Service
public class RedisService<T> {

    @Resource
    private RedisTemplate<String, T> redisTemplate;

    /**
     * 设置值及过期时间
     * @param key
     * @param value
     * @param timeout
     * @param unit
     */
    public void set(String key, T value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 通过key获取value
     * @param key
     * @return
     */
    public T get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 分布式锁
     * @param key
     * @param value
     * @param timeout
     * @param unit
     * @return false: 获取不到锁 true: 获取到锁
     */
    public Boolean tryLock(String key, T value, long timeout, TimeUnit unit) {
        if (null != redisTemplate.opsForValue().get(key)) {
            return false;
        }

        set(key, value, timeout, unit);
        return true;
    }
}
