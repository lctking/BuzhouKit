package com.lctking.buzhoukitcore.cache.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.lctking.buzhoukitcore.cache.service.LocalCacheService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CaffeineCacheServiceImpl<K,V> implements LocalCacheService<K,V> {
    private final Map<String, Cache<K,V>> CACHE_HOLDER;
    //private static final String UNIQUE_EXPIRE_KEY = "unique_expireTime_%s_timeunit_%s";

    public CaffeineCacheServiceImpl() {
        CACHE_HOLDER = new HashMap<>();
    }

    public Cache<K,V> createSingleCaffeineCache(int initSize, int maxSize, long expireTime, TimeUnit timeUnit, boolean isWeakKeys) {
        if(isWeakKeys){
            return Caffeine.newBuilder()
                    .expireAfterWrite(expireTime,timeUnit)
                    .initialCapacity(initSize)
                    .maximumSize(maxSize)
                    .weakKeys()
                    .build();
        }else return Caffeine.newBuilder()
                .expireAfterWrite(expireTime,timeUnit)
                .initialCapacity(initSize)
                .maximumSize(maxSize)
                .build();
    }


    @Override
    public V get(K key) {
        Cache<K, V> caffeineCache = getCaffeineCache(key);
        return caffeineCache.getIfPresent(key);
    }

    @Override
    public V setIfAbsent(K key, V value, long expireTime, TimeUnit timeUnit) {
        String shardingKey = getShardingKey((String) key);
        Cache<K, V> caffeineCache = initialCaffeineCache(shardingKey, expireTime, timeUnit);
        V oldVal = caffeineCache.getIfPresent(key);
        if(oldVal != null)return oldVal;
        caffeineCache.put(key,value);
        return null;
    }

    @Override
    public void remove(K key) {
        Cache<K, V> caffeineCache = getCaffeineCache(key);
        caffeineCache.invalidate(key);
        caffeineCache.cleanUp();
    }

    @Override
    public void clear() {
        CACHE_HOLDER.clear();
    }

    @Override
    public V put(K key, V val) {
        Cache<K, V> caffeineCache = getCaffeineCache(key);
        V oldVal = caffeineCache.getIfPresent(key);
        caffeineCache.put(key,val);
        return oldVal;
    }

    @Override
    public V put(K key, V value, long expireTime, TimeUnit timeUnit) {
        String shardingKey = getShardingKey((String) key);
        Cache<K, V> caffeineCache = initialCaffeineCache(shardingKey, expireTime, timeUnit);
        V oldVal = caffeineCache.getIfPresent(key);
        caffeineCache.put(key,value);
        return oldVal;
    }

    @Override
    public boolean containsKey(K key) {
        Cache<K, V> caffeineCache = getCaffeineCache(key);
        return caffeineCache.asMap().containsKey(key);
    }

    private Cache<K, V> initialCaffeineCache(String shardingKey, long expireTime,TimeUnit timeUnit){
        long seconds = timeUnit.toSeconds(expireTime);
        Cache<K, V> caffeineCache = CACHE_HOLDER.get(shardingKey);
        // initial the cache only when it is null
        if(caffeineCache != null)return caffeineCache;
        synchronized (shardingKey){
            caffeineCache = createSingleCaffeineCache(16,1024, seconds, TimeUnit.SECONDS,false);
            CACHE_HOLDER.putIfAbsent(shardingKey, caffeineCache);
            return CACHE_HOLDER.get(shardingKey);
        }
    }

    // 将key由最后一个冒号字符进行分割，例如prefix1:prefix2:prefix3:spELValue中的prefix1:prefix2:prefix3即为分片前缀
    private static String getShardingKey(String key){
        int idx = key.lastIndexOf(":");
        if(idx == -1)throw new RuntimeException("the format of key is wrong");
        return key.substring(0,idx);
    }

    @Override
    public void uniquePrefixInitial(String shardingKey, long expireTime, TimeUnit timeUnit) {
        if(CACHE_HOLDER.containsKey(shardingKey))return;
        initialCaffeineCache(shardingKey, expireTime, timeUnit);
    }

    private Cache<K, V> getCaffeineCache(K key){
        String shardingKey = getShardingKey((String) key);
        return CACHE_HOLDER.get(shardingKey);
    }

}
