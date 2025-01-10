package com.lctking.buzhoukitidempotent.cache.factory;

import com.lctking.buzhoukitidempotent.cache.impl.BloomFilterCacheServiceImpl;
import com.lctking.buzhoukitidempotent.cache.impl.CaffeineCacheServiceImpl;
import com.lctking.buzhoukitidempotent.cache.impl.RedisCacheServiceImpl;
import com.lctking.buzhoukitidempotent.cache.service.BloomFilterCacheService;
import com.lctking.buzhoukitidempotent.cache.service.DistributeCacheService;

import static cn.hutool.extra.spring.SpringUtil.getBean;

public final class CacheFactory {
    private CacheFactory(){};

    private static final String CAFFEINE_CACHE_INITIAL_LOCK = "caffeine_cache_initial_lock";

    private static final String BLOOM_FILTER_CACHE_INITIAL_LOCK = "bloom_filter_cache_initial_lock";

    private static CaffeineCacheServiceImpl<String, Object> CAFFEINE_CACHE;

    private static final DistributeCacheService<String, Object> REDIS_CACHE = getBean(RedisCacheServiceImpl.class);

    private static BloomFilterCacheService BLOOM_FILTER_CACHE_SERVICE;


    public static CaffeineCacheServiceImpl<String, Object> getCaffeineCache(){
        if(CAFFEINE_CACHE != null)return CAFFEINE_CACHE;
        synchronized (CAFFEINE_CACHE_INITIAL_LOCK){
            if(CAFFEINE_CACHE != null)return CAFFEINE_CACHE;
            CAFFEINE_CACHE = new CaffeineCacheServiceImpl<>();
            return CAFFEINE_CACHE;
        }
    }

    public static DistributeCacheService<String, Object> getRedisCache(){
        return REDIS_CACHE;
    }

    public static BloomFilterCacheService getBloomFilter(){
        if(BLOOM_FILTER_CACHE_SERVICE != null)return BLOOM_FILTER_CACHE_SERVICE;
        synchronized (BLOOM_FILTER_CACHE_INITIAL_LOCK){
            if(BLOOM_FILTER_CACHE_SERVICE != null)return BLOOM_FILTER_CACHE_SERVICE;
            BLOOM_FILTER_CACHE_SERVICE = getBean(BloomFilterCacheServiceImpl.class);
            return BLOOM_FILTER_CACHE_SERVICE;
        }
    }

}
