package com.lctking.buzhoukitcore.cache;

import com.lctking.buzhoukitcore.exception.cacheException;

import java.util.concurrent.ConcurrentHashMap;

public class HashMapCacheServiceImpl<K,V> implements CacheService<K,V> {

    private final ConcurrentHashMap<K,V> cache;
    private final int maxSize;

    public HashMapCacheServiceImpl(int initSize, int maxSize){
        this.cache = new ConcurrentHashMap<>(initSize);
        this.maxSize = maxSize;
    }

    @Override
    public void put(K key, V val) throws cacheException {
        if(this.maxSize == cache.size()){
            //cache is full
            throw new cacheException("cache is full");
        }
        this.cache.put(key,val);
    }

    @Override
    public V get(K key) {
        return this.cache.get(key);
    }

    @Override
    public void clear() {
        this.cache.clear();
    }

    @Override
    public V putAndGetOld(K key, V val) {
        V oldVal = this.cache.get(key);
        this.cache.put(key,val);
        return oldVal;
    }

    @Override
    public boolean containsKey(K key) {
        return this.cache.containsKey(key);
    }

    @Override
    public void remove(K key) {
        this.cache.remove(key);
    }
}
