package com.lctking.buzhoukitidempotent.executor.impl;

import com.lctking.buzhoukitidempotent.annotation.Idempotent;
import com.lctking.buzhoukitidempotent.cache.service.DistributeCacheService;
import com.lctking.buzhoukitidempotent.exception.IdempotentException;
import com.lctking.buzhoukitidempotent.executor.IdempotentArgsWrapper;
import com.lctking.buzhoukitidempotent.executor.service.IdempotentExecuteDistributeService;
import com.lctking.buzhoukitidempotent.utils.SpELParser;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class IdempotentExecuteDistributeServiceImpl implements IdempotentExecuteDistributeService {
    private final DistributeCacheService<String,Object> cacheService;

    @Override
    public void proceed(ProceedingJoinPoint joinPoint, Idempotent idempotent) {
        execute(wrapperBuilder(joinPoint,idempotent));
    }

    @SneakyThrows
    @Override
    public void execute(IdempotentArgsWrapper wrapper) {
        Idempotent idempotent = wrapper.getIdempotent();
        long expireTime = idempotent.expireTime();
        TimeUnit timeUnit = idempotent.timeUnit();
        String keyForLock = wrapper.getKeyForLock();
        String result = (String) cacheService.setIfAbsent(keyForLock, "-", expireTime, timeUnit);
        // result不为空说明插入失败
        if(result != null){
            throw new IdempotentException(idempotent.message());
        }

    }

    @Override
    public void exceptionProcess() {
        IdempotentExecuteDistributeService.super.exceptionProcess();
    }

    @Override
    public void postProcess() {
        IdempotentExecuteDistributeService.super.postProcess();
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
