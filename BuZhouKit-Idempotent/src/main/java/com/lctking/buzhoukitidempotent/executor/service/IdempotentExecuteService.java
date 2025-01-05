package com.lctking.buzhoukitidempotent.executor.service;

import com.lctking.buzhoukitidempotent.annotation.Idempotent;
import com.lctking.buzhoukitidempotent.executor.IdempotentArgsWrapper;
import org.aspectj.lang.ProceedingJoinPoint;

public interface IdempotentExecuteService {
    void proceed(ProceedingJoinPoint joinPoint, Idempotent idempotent);

    void execute(IdempotentArgsWrapper wrapper);

    default void exceptionProcess(){}

    /**
     * 后置处理
     */
    default void postProcess(){}
}
