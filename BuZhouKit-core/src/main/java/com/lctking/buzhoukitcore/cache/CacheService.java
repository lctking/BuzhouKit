package com.lctking.buzhoukitcore.cache;

public interface CacheService<K,V>{
    /**
     *
     */
    void put(K key, V val);

    /**
     *
     */
    V get(K key);

    /**
     *
     */
    void clear();


    V putAndGetOld(K key, V val);

    boolean containsKey(K key);

    void remove(K key);
}
