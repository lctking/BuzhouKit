package com.lctking.buzhoukitcore.cache.factory;

import com.lctking.buzhoukitcore.cache.impl.CaffeineCacheServiceImpl;
import com.lctking.buzhoukitcore.cache.impl.RedisCacheServiceImpl;
import com.lctking.buzhoukitcore.cache.service.DistributeCacheService;
import static cn.hutool.extra.spring.SpringUtil.getBean;

public final class CacheFactory {
    private CacheFactory(){};

    private static final String CAFFEINE_CACHE_INITIAL_LOCK = "caffeine_cache_initial_lock";

    private static CaffeineCacheServiceImpl<String, Object> CAFFEINE_CACHE;

    private static final DistributeCacheService<String, Object> REDIS_CACHE = getBean(RedisCacheServiceImpl.class);



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

}
