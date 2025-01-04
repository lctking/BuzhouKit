package com.lctking.buzhoukitcore.executor.impl;

import com.lctking.buzhoukitcore.annotation.Idempotent;
import com.lctking.buzhoukitcore.cache.service.LocalCacheService;
import com.lctking.buzhoukitcore.executor.IdempotentArgsWrapper;
import com.lctking.buzhoukitcore.executor.service.IdempotentExecuteLocalService;
import com.lctking.buzhoukitcore.executor.service.IdempotentExecuteService;
import com.lctking.buzhoukitcore.utils.SpELParser;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class IdempotentExecuteLocalServiceImpl implements IdempotentExecuteLocalService {
    private final LocalCacheService<String,Object> cacheService;

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
            throw new Exception(idempotent.message());
        }

    }

    @Override
    public void exceptionProcess() {
        IdempotentExecuteLocalService.super.exceptionProcess();
    }

    @Override
    public void postProcess() {
        IdempotentExecuteLocalService.super.postProcess();
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
