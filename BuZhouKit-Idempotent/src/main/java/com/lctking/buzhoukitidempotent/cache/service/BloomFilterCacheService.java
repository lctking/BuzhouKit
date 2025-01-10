package com.lctking.buzhoukitidempotent.cache.service;

public interface BloomFilterCacheService {
    Boolean contains(String key);
    Boolean setIfAbsent(String key);

}
