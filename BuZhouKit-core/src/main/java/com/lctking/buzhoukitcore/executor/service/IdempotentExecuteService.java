package com.lctking.buzhoukitcore.executor.service;

import com.lctking.buzhoukitcore.annotation.Idempotent;
import com.lctking.buzhoukitcore.executor.IdempotentArgsWrapper;
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
