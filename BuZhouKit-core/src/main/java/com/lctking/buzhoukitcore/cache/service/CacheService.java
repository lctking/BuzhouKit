package com.lctking.buzhoukitcore.cache.service;

import java.util.concurrent.TimeUnit;

public interface CacheService<K,V>{
    /**
     *
     */
    V put(K key, V val);

    V put(K key, V value, long expireTime, TimeUnit timeUnit);
    /**
     *
     */
    V get(K key);

    V setIfAbsent(K key, V value, long expireTime, TimeUnit timeUnit);
    /**
     *
     */
    void clear();

    boolean containsKey(K key);

    void remove(K key);
}
