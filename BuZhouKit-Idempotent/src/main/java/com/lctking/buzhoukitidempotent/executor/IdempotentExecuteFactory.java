package com.lctking.buzhoukitidempotent.executor;

import com.lctking.buzhoukitidempotent.cache.factory.CacheFactory;
import com.lctking.buzhoukitidempotent.constant.CacheTypeEnum;
import com.lctking.buzhoukitidempotent.executor.impl.BloomFilterCacheIdempotentExecuteServiceImpl;
import com.lctking.buzhoukitidempotent.executor.impl.DistributedCacheIdempotentExecuteServiceImpl;
import com.lctking.buzhoukitidempotent.executor.impl.LocalCacheIdempotentExecuteServiceImpl;
import com.lctking.buzhoukitidempotent.executor.service.IdempotentExecuteService;

public class IdempotentExecuteFactory {

    private static final IdempotentExecuteService IDEMPOTENT_EXECUTE_SERVICE_BASED_REDIS;
    private static final IdempotentExecuteService IDEMPOTENT_EXECUTE_SERVICE_BASED_CAFFEINE;
    private static final IdempotentExecuteService IDEMPOTENT_EXECUTE_SERVICE_BASED_BLOOM_FILTER;

    static {
        IDEMPOTENT_EXECUTE_SERVICE_BASED_REDIS = new DistributedCacheIdempotentExecuteServiceImpl(CacheFactory.getRedisCache());
        IDEMPOTENT_EXECUTE_SERVICE_BASED_CAFFEINE = new LocalCacheIdempotentExecuteServiceImpl(CacheFactory.getCaffeineCache());
        IDEMPOTENT_EXECUTE_SERVICE_BASED_BLOOM_FILTER = new BloomFilterCacheIdempotentExecuteServiceImpl(CacheFactory.getBloomFilter());
    }

    public static IdempotentExecuteService getInstance(CacheTypeEnum cacheTypeEnum){
        switch (cacheTypeEnum){
            case LOCAL, CAFFEINE -> {
                return IDEMPOTENT_EXECUTE_SERVICE_BASED_CAFFEINE;
            }
            case REDIS -> {
                return IDEMPOTENT_EXECUTE_SERVICE_BASED_REDIS;
            }
            case BLOOM -> {
                return IDEMPOTENT_EXECUTE_SERVICE_BASED_BLOOM_FILTER;
            }
        }
        return IDEMPOTENT_EXECUTE_SERVICE_BASED_REDIS;

    }

}
