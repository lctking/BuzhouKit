package com.lctking.buzhoukitidempotent.config;

import com.lctking.buzhoukitidempotent.aspect.IdempotentAspect;
import com.lctking.buzhoukitidempotent.cache.impl.BloomFilterCacheServiceImpl;
import com.lctking.buzhoukitidempotent.cache.impl.CaffeineCacheServiceImpl;
import com.lctking.buzhoukitidempotent.cache.impl.RedisCacheServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(value = {BloomFilterCacheProperties.class})
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

    @Bean
    @ConditionalOnProperty(prefix = BloomFilterCacheProperties.PREFIX, name = "enabled" ,havingValue = "true")
    public RBloomFilter<String> bloomFilterCache(RedissonClient redissonClient, BloomFilterCacheProperties bloomFilterCacheProperties){
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter(bloomFilterCacheProperties.getName());
        bloomFilter.tryInit(bloomFilterCacheProperties.getExpectedInsertions(), bloomFilterCacheProperties.getFalseProbability());
        return bloomFilter;
    }

    @Bean
    public BloomFilterCacheServiceImpl BloomFilterCacheServiceImpl(RBloomFilter<String> bloomFilterCache){
        return new BloomFilterCacheServiceImpl(bloomFilterCache);
    }
}
