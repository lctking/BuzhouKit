package com.lctking.buzhoukitcore.cache.impl;

import com.lctking.buzhoukitcore.cache.service.LocalCacheService;
import com.lctking.buzhoukitcore.exception.CacheException;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class HashMapCacheServiceImpl<K,V> implements LocalCacheService<K,V> {

    private final ConcurrentHashMap<K,V> cache;
    private final int maxSize;

    public HashMapCacheServiceImpl(int initSize, int maxSize){
        this.cache = new ConcurrentHashMap<>(initSize);
        this.maxSize = maxSize;
    }


    @Override
    public V get(K key) {
        return this.cache.get(key);
    }

    @Override
    public V setIfAbsent(K key, V value, long expireTime, TimeUnit timeUnit) {
        return null;
    }

    @Override
    public void clear() {
        this.cache.clear();
    }

    @Override
    public V put(K key, V val) {
        if(this.maxSize == cache.size()){
            //cache is full
            throw new CacheException("cache is full");
        }
        V oldVal = this.cache.get(key);
        this.cache.put(key,val);
        return oldVal;
    }

    @Override
    public V put(K key, V value, long expireTime, TimeUnit timeUnit) {
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        return this.cache.containsKey(key);
    }

    @Override
    public void remove(K key) {
        this.cache.remove(key);
    }

    @Override
    public void uniquePrefixInitial(String uniquePrefix, long expireTime, TimeUnit timeUnit) {

    }
}
