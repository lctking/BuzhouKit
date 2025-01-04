package com.lctking.buzhoukitcore.config;

import com.lctking.buzhoukitcore.aspect.IdempotentAspect;
import com.lctking.buzhoukitcore.cache.impl.CaffeineCacheServiceImpl;
import com.lctking.buzhoukitcore.cache.service.CacheService;
import com.lctking.buzhoukitcore.cache.impl.RedisCacheServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
@RequiredArgsConstructor
public class IdempotentAutoConfiguration {
    private final StringRedisTemplate stringRedisTemplate;

    @Bean
    public IdempotentAspect idempotentAspect() {
        return new IdempotentAspect();
    }

    @Bean
    public RedisCacheServiceImpl<Object, Object> RedisCacheServiceImpl(){
        return new RedisCacheServiceImpl<>(stringRedisTemplate);
    }

    @Bean CaffeineCacheServiceImpl<Object, Object> CaffeineCacheServiceImpl(){
        return new CaffeineCacheServiceImpl<>();
    }
}
