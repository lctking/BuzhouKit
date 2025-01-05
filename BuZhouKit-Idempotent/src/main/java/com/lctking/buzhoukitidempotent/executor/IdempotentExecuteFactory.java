package com.lctking.buzhoukitidempotent.executor;

import com.lctking.buzhoukitidempotent.cache.factory.CacheFactory;
import com.lctking.buzhoukitidempotent.constant.CacheTypeEnum;
import com.lctking.buzhoukitidempotent.executor.impl.DistributedCacheIdempotentExecuteServiceImpl;
import com.lctking.buzhoukitidempotent.executor.impl.LocalCacheIdempotentExecuteServiceImpl;
import com.lctking.buzhoukitidempotent.executor.service.IdempotentExecuteService;

public class IdempotentExecuteFactory {

    private static final IdempotentExecuteService IDEMPOTENT_EXECUTE_SERVICE_BASED_REDIS;
    private static final IdempotentExecuteService IDEMPOTENT_EXECUTE_SERVICE_BASED_CAFFEINE;
    static {
        IDEMPOTENT_EXECUTE_SERVICE_BASED_REDIS = new DistributedCacheIdempotentExecuteServiceImpl(CacheFactory.getRedisCache());
        IDEMPOTENT_EXECUTE_SERVICE_BASED_CAFFEINE = new LocalCacheIdempotentExecuteServiceImpl(CacheFactory.getCaffeineCache());
    }

    public static IdempotentExecuteService getInstance(CacheTypeEnum cacheTypeEnum){
        switch (cacheTypeEnum){
            case LOCAL, CAFFEINE -> {
                return IDEMPOTENT_EXECUTE_SERVICE_BASED_CAFFEINE;
            }
            case REDIS -> {
                return IDEMPOTENT_EXECUTE_SERVICE_BASED_REDIS;
            }
        }
        return IDEMPOTENT_EXECUTE_SERVICE_BASED_REDIS;

    }

}
