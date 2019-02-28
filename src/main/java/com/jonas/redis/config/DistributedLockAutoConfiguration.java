package com.jonas.redis.config;

import com.jonas.redis.service.lock.DistributedLock;
import com.jonas.redis.service.lock.RedisDistributedLock;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 分布式锁配置
 *
 * @author shenjy 2019/02/28
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class DistributedLockAutoConfiguration {

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory factory) {
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        // 对于普通K-V操作时，key采取的序列化策略
        template.setKeySerializer(new StringRedisSerializer());
        // value采取的序列化策略
        template.setValueSerializer(serializer);
        // 在hash数据结构中，hash-key的序列化策略
        template.setHashKeySerializer(serializer);
        // 在hash数据结构中，hash-key的序列化策略
        template.setHashValueSerializer(serializer);
        template.setConnectionFactory(factory);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisCacheManager.RedisCacheManagerBuilder builder =
                RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(factory);
        return builder.build();
    }

    @Bean
    @ConditionalOnBean(RedisTemplate.class)
    public DistributedLock redisDistributedLock(RedisTemplate redisTemplate){
        return new RedisDistributedLock(redisTemplate);
    }

}
