package com.lctking.buzhoukitidempotent.config;

import com.lctking.buzhoukitidempotent.aspect.IdempotentAspect;
import com.lctking.buzhoukitidempotent.cache.impl.CaffeineCacheServiceImpl;
import com.lctking.buzhoukitidempotent.cache.impl.RedisCacheServiceImpl;
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

    @Bean
    CaffeineCacheServiceImpl<Object, Object> CaffeineCacheServiceImpl(){
        return new CaffeineCacheServiceImpl<>();
    }
}
