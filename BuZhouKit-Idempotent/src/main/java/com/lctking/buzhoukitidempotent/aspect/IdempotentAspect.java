package com.lctking.buzhoukitidempotent.aspect;

import com.lctking.buzhoukitidempotent.annotation.Idempotent;
import com.lctking.buzhoukitidempotent.executor.IdempotentExecuteFactory;
import com.lctking.buzhoukitidempotent.executor.service.IdempotentExecuteService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
public class IdempotentAspect {

    @Around("@annotation(com.lctking.buzhoukitidempotent.annotation.Idempotent)")
    public Object handle(ProceedingJoinPoint joinPoint){
        Idempotent idempotent = IdempotentGetter(joinPoint);
        IdempotentExecuteService instance = IdempotentExecuteFactory.getInstance(idempotent.cacheType());
        Object result = null;
        try{
            instance.proceed(joinPoint,idempotent);
            result = joinPoint.proceed();
        } catch (Throwable e) {
            instance.exceptionProcess();
            throw new RuntimeException(e);
        } finally {
            instance.postProcess();
        }
        return result;
    }


    /**
     * 获取切点处的注解
     * @param joinPoint
     * @return Idempotent
     */
    private static Idempotent IdempotentGetter(ProceedingJoinPoint joinPoint){
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        //Method targetMethod = joinPoint.getTarget().getClass().getDeclaredMethod(methodSignature.getName(), methodSignature.getMethod().getParameterTypes());
        return methodSignature.getMethod().getAnnotation(Idempotent.class);
    }

}
