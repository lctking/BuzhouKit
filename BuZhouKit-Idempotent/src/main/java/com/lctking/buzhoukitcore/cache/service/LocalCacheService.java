package com.lctking.buzhoukitcore.cache.service;

import java.util.concurrent.TimeUnit;

public interface LocalCacheService<K, V> extends CacheService<K, V> {
    void uniquePrefixInitial(String uniquePrefix, long expireTime, TimeUnit timeUnit);
}
