package com.lctking.buzhoukitcore.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

public class CaffeineCacheServiceImpl<K,V> implements CacheService<K,V> {
    private final Cache<K,V> cache;

    public CaffeineCacheServiceImpl(int initSize, int maxSize, long expireTime, TimeUnit timeUnit, boolean isWeakKeys) {
        if(isWeakKeys){
            cache = Caffeine.newBuilder()
                    .expireAfterWrite(expireTime,timeUnit)
                    .initialCapacity(initSize)
                    .maximumSize(maxSize)
                    .weakKeys()
                    .build();
        }else cache = Caffeine.newBuilder()
                .expireAfterWrite(expireTime,timeUnit)
                .initialCapacity(initSize)
                .maximumSize(maxSize)
                .build();
    }


    @Override
    public void put(K key, V val) {
        this.cache.put(key,val);
    }

    @Override
    public V get(K key) {
        return this.cache.getIfPresent(key);
    }

    @Override
    public void remove(K key) {
        this.cache.invalidate(key);
        this.cache.cleanUp();
    }

    @Override
    public void clear() {
        this.cache.invalidateAll();
        this.cache.cleanUp();
    }

    @Override
    public V putAndGetOld(K key, V val) {
        V oldVal = this.cache.getIfPresent(key);
        this.cache.put(key,val);
        return oldVal;
    }

    @Override
    public boolean containsKey(K key) {
        return this.cache.asMap().containsKey(key);
    }

}
