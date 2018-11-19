package com.yushi.redis.config;

import com.yushi.redis.service.RedisService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 【 enter the class description 】
 *
 * @author shenjy 2018/11/19
 */
@Configuration
@EnableCaching
@ConditionalOnClass(RedisOperations.class)
@EnableConfigurationProperties(RedisProperties.class)
public class RedisAutoConfigure {

    @Bean
    public RedisService redisService() {
        return new RedisService();
    }

    @Bean(name = "redisTemplate")
    @ConditionalOnMissingBean(name = "redisTemplate")
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
}
