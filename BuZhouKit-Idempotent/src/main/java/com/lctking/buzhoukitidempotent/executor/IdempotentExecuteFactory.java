package com.lctking.buzhoukitidempotent.executor;

import com.lctking.buzhoukitidempotent.cache.factory.CacheFactory;
import com.lctking.buzhoukitidempotent.constant.CacheTypeEnum;
import com.lctking.buzhoukitidempotent.executor.impl.IdempotentExecuteDistributeServiceImpl;
import com.lctking.buzhoukitidempotent.executor.impl.IdempotentExecuteLocalServiceImpl;
import com.lctking.buzhoukitidempotent.executor.service.IdempotentExecuteService;

public class IdempotentExecuteFactory {

    private static final IdempotentExecuteService IDEMPOTENT_EXECUTE_SERVICE_BASED_REDIS;
    private static final IdempotentExecuteService IDEMPOTENT_EXECUTE_SERVICE_BASED_CAFFEINE;
    static {
        IDEMPOTENT_EXECUTE_SERVICE_BASED_REDIS = new IdempotentExecuteDistributeServiceImpl(CacheFactory.getRedisCache());
        IDEMPOTENT_EXECUTE_SERVICE_BASED_CAFFEINE = new IdempotentExecuteLocalServiceImpl(CacheFactory.getCaffeineCache());
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
