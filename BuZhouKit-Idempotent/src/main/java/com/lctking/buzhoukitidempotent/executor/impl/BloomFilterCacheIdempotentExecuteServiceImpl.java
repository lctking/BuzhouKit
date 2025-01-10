package com.lctking.buzhoukitidempotent.executor.impl;

import com.lctking.buzhoukitidempotent.annotation.Idempotent;
import com.lctking.buzhoukitidempotent.cache.service.BloomFilterCacheService;
import com.lctking.buzhoukitidempotent.executor.IdempotentArgsWrapper;
import com.lctking.buzhoukitidempotent.executor.service.BloomFilterCacheIdempotentExecuteService;
import com.lctking.buzhoukitidempotent.utils.ExceptionThrower;
import com.lctking.buzhoukitidempotent.utils.SpELParser;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

@AllArgsConstructor
public class BloomFilterCacheIdempotentExecuteServiceImpl implements BloomFilterCacheIdempotentExecuteService {
    private final BloomFilterCacheService bloomFilterCacheService;

    @Override
    public void proceed(ProceedingJoinPoint joinPoint, Idempotent idempotent) {
        execute(wrapperBuilder(joinPoint, idempotent));
    }

    @SneakyThrows
    @Override
    public void execute(IdempotentArgsWrapper wrapper) {
        Idempotent idempotent = wrapper.getIdempotent();
        String keyForLock = wrapper.getKeyForLock();
        Boolean setIfAbsent = bloomFilterCacheService.setIfAbsent(keyForLock);
        if(setIfAbsent == null || !setIfAbsent){
            Class<? extends Throwable> exceptionClass = idempotent.exceptionClass();
            ExceptionThrower.throwException(exceptionClass, idempotent.message());
        }
    }

    @Override
    public void exceptionProcess() {
        BloomFilterCacheIdempotentExecuteService.super.exceptionProcess();
    }

    @Override
    public void postProcess() {
        BloomFilterCacheIdempotentExecuteService.super.postProcess();
    }

    public IdempotentArgsWrapper wrapperBuilder(ProceedingJoinPoint joinPoint, Idempotent idempotent){
        Object parsedValue = SpELParser.parse(idempotent.spEL(), ((MethodSignature) joinPoint.getSignature()).getMethod(), joinPoint.getArgs());
        String spELValue = "";
        try{
            spELValue = (String) parsedValue;
        }catch (ClassCastException e){
            // 如果parsedValue是基本数据类型，则使用String.valueOf
            spELValue = String.valueOf(parsedValue);
        }
        String keyForLock = idempotent.uniquePrefix()+":"+spELValue;

        return IdempotentArgsWrapper.builder()
                .joinPoint(joinPoint)
                .idempotent(idempotent)
                .keyForLock(keyForLock)
                .build();
    }
}
