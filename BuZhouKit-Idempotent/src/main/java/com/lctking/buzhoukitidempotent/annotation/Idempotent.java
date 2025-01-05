package com.lctking.buzhoukitidempotent.annotation;


import com.lctking.buzhoukitidempotent.constant.CacheTypeEnum;
import com.lctking.buzhoukitidempotent.constant.IdempotentTypeEnum;
import com.lctking.buzhoukitidempotent.exception.IdempotentException;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Idempotent {


    IdempotentTypeEnum type() default IdempotentTypeEnum.TOKEN;

    /**
     * 触发幂等失败逻辑时，返回的错误提示信息
     */
    String message() default "操作太频繁，请稍后再试";

    String spEL() default "";

    String uniquePrefix() default "";

    long expireTime() default 3600L;

    TimeUnit timeUnit() default TimeUnit.SECONDS;

    CacheTypeEnum cacheType() default CacheTypeEnum.REDIS;

    Class<? extends Throwable> exceptionClass() default IdempotentException.class;
}
