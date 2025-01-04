package com.lctking.test.controller;

import com.lctking.buzhoukitcore.annotation.Idempotent;
import com.lctking.buzhoukitcore.constant.CacheTypeEnum;
import com.lctking.buzhoukitcore.constant.IdempotentTypeEnum;
import com.lctking.test.entity.UserDO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class testController {
    private final StringRedisTemplate stringRedisTemplate;

    @PostMapping("/test")
    @Idempotent(type = IdempotentTypeEnum.TOKEN,
            uniquePrefix = "user:insert",
            spEL = "#userDO.hashCode()",
            expireTime = 20000L,
            cacheType = CacheTypeEnum.REDIS,
            message = "用户添加请求重复,[BY_REDIS]"
    )
    public void testIdempotent(@RequestBody UserDO userDO){
        insertUser(userDO);
    }

    @PostMapping("/test2")
    @Idempotent(type = IdempotentTypeEnum.TOKEN,
            uniquePrefix = "user:insert",
            spEL = "#userDO.hashCode()",
            expireTime = 20000L,
            cacheType = CacheTypeEnum.LOCAL,
            message = "用户添加请求重复,[BY_LOCAL]"
    )
    public void testIdempotentByLocalCache(@RequestBody UserDO userDO){
        insertUser(userDO);
    }

    void insertUser(UserDO userDO){
        System.out.println(userDO.toString());
        System.out.println(userDO.hashCode());
    }

}
