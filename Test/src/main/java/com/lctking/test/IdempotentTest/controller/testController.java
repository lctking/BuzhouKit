package com.lctking.test.IdempotentTest.controller;

import com.lctking.buzhoukitidempotent.annotation.Idempotent;
import com.lctking.buzhoukitidempotent.constant.CacheTypeEnum;
import com.lctking.buzhoukitidempotent.constant.IdempotentTypeEnum;
import com.lctking.test.IdempotentTest.entity.UserDO;
import com.lctking.test.IdempotentTest.exception.CustomizeException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Idempotent-test")
public class testController {
    private final StringRedisTemplate stringRedisTemplate;

    @PostMapping("/redisCache-test")
    @Idempotent(type = IdempotentTypeEnum.TOKEN,
            uniquePrefix = "user:insert",
            spEL = "#userDO.hashCode()",
            expireTime = -1L,
            cacheType = CacheTypeEnum.REDIS,
            message = "用户添加请求重复,[BY_REDIS]",
            exceptionClass = CustomizeException.class
    )
    public void testIdempotentOnDistributedCache(@RequestBody UserDO userDO){
        if(userDO != null && userDO.getId() == 1L){
            throw new RuntimeException();
        }
        insertUser(userDO);
    }

    @PostMapping("/localCache-test")
    @Idempotent(type = IdempotentTypeEnum.TOKEN,
            uniquePrefix = "user:insert",
            spEL = "#userDO.hashCode()",
            expireTime = -1L,
            cacheType = CacheTypeEnum.LOCAL,
            message = "用户添加请求重复,[BY_LOCAL]",
            exceptionClass = CustomizeException.class
    )
    public void testIdempotentOnLocalCache(@RequestBody UserDO userDO){
        if(userDO != null && userDO.getId() == 1L){
            throw new RuntimeException();
        }
        insertUser(userDO);
    }

    @PostMapping("/BloomFilterCache-test")
    @Idempotent(type = IdempotentTypeEnum.TOKEN,
            uniquePrefix = "user:insert",
            spEL = "#userDO.hashCode()",
            cacheType = CacheTypeEnum.BLOOM,
            message = "用户添加请求重复,[BY_BLOOM]",
            exceptionClass = CustomizeException.class
    )
    public void testIdempotentOnBloomCache(@RequestBody UserDO userDO){
        insertUser(userDO);
    }

    void insertUser(UserDO userDO){
        System.out.println(userDO.toString());
        System.out.println(userDO.hashCode());

    }

}
