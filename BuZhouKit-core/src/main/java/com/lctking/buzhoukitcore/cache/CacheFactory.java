package com.lctking.buzhoukitcore.cache;

import java.util.concurrent.TimeUnit;

public final class CacheFactory {
    private CacheFactory(){};

    private static final String lock = "caffeine_cache_initial_lock";

    private static final CacheService<String, Object> HASHMAP_CACHE = new HashMapCacheServiceImpl<>(16,1024);

    private static CacheService<String, Object> CAFFEINE_CACHE;


    public static CacheService<String, Object> getCaffeineCache(){
        if(CAFFEINE_CACHE != null)return CAFFEINE_CACHE;

        synchronized (lock){
            if(CAFFEINE_CACHE != null)return CAFFEINE_CACHE;
            initialCaffeineCache(40L, TimeUnit.MINUTES);
        }
        return CAFFEINE_CACHE;
    }

    public static CacheService<String, Object> getCaffeineCache(long expireTime, TimeUnit timeUnit){
        if(CAFFEINE_CACHE != null)return CAFFEINE_CACHE;

        synchronized (lock){
            if(CAFFEINE_CACHE != null)return CAFFEINE_CACHE;
            initialCaffeineCache(expireTime, timeUnit);
        }
        return CAFFEINE_CACHE;
    }


    private static void initialCaffeineCache(long expireTime,TimeUnit timeUnit){
        // initial the cache only when it is null
        if(CAFFEINE_CACHE != null){
            return;
        }
        CAFFEINE_CACHE = new CaffeineCacheServiceImpl<>(16,1024, expireTime, timeUnit,false);
    }

    public static CacheService<String, Object> getHashmapCache(){
        return HASHMAP_CACHE;
    }

}
