package com.lctking.buzhoukitidempotent.cache.impl;

import com.lctking.buzhoukitidempotent.cache.service.BloomFilterCacheService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;

@RequiredArgsConstructor
public class BloomFilterCacheServiceImpl implements BloomFilterCacheService {
    private final RBloomFilter<String> bloomFilterCache;
    @Override
    public Boolean contains(String key) {
        return bloomFilterCache.contains(key);
    }

    @Override
    public Boolean setIfAbsent(String key) {
        if(bloomFilterCache.contains(key)){
            return false;
        }
        return bloomFilterCache.add(key);
    }
}