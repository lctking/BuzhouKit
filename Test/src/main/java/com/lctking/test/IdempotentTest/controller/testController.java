package com.lctking.test.IdempotentTest.controller;

import com.lctking.buzhoukitcore.annotation.Idempotent;
import com.lctking.buzhoukitcore.constant.CacheTypeEnum;
import com.lctking.buzhoukitcore.constant.IdempotentTypeEnum;
import com.lctking.test.IdempotentTest.entity.UserDO;
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
            expireTime = 20000L,
            cacheType = CacheTypeEnum.REDIS,
            message = "用户添加请求重复,[BY_REDIS]"
    )
    public void testIdempotentOnDistributedCache(@RequestBody UserDO userDO){
        insertUser(userDO);
    }

    @PostMapping("/localCache-test")
    @Idempotent(type = IdempotentTypeEnum.TOKEN,
            uniquePrefix = "user:insert",
            spEL = "#userDO.hashCode()",
            expireTime = 20000L,
            cacheType = CacheTypeEnum.LOCAL,
            message = "用户添加请求重复,[BY_LOCAL]"
    )
    public void testIdempotentOnLocalCache(@RequestBody UserDO userDO){
        insertUser(userDO);
    }

    void insertUser(UserDO userDO){
        System.out.println(userDO.toString());
        System.out.println(userDO.hashCode());

    }

}
