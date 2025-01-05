package com.lctking.buzhoukitidempotent.cache.impl;

import com.lctking.buzhoukitidempotent.cache.service.DistributeCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class RedisCacheServiceImpl<K,V> implements DistributeCacheService<K,V> {
    private final StringRedisTemplate cache;
    private static final String LUA_SCRIPT_PUT_IF_ABSENT_OR_GET_OLD__PATH = "lua/put_if_absent_or_get_old.lua";

    //todo
    @Override
    public V put(K key, V val) {
        return put(key, val, -1, TimeUnit.MINUTES);
    }

    @Override
    public V put(K key, V val, long expireTime, TimeUnit timeUnit) {
//        DefaultRedisScript<String> redisScript = new DefaultRedisScript<>();
//        ClassPathResource resource = new ClassPathResource(LUA_SCRIPT_PUT_IF_ABSENT_OR_GET_OLD__PATH);
//        redisScript.setScriptSource(new ResourceScriptSource(resource));
//        redisScript.setResultType(String.class);
//
//        long millis = timeUnit.toMillis(expireTime);
//        String result = cache.execute(redisScript, List.of((String) key), val, String.valueOf(millis));
//        return (V) result;
        return null;
    }

    @Override
    public V get(K key) {
        return null;
    }

    @Override
    public V setIfAbsent(K key, V value, long expireTime, TimeUnit timeUnit) {
        DefaultRedisScript<String> redisScript = new DefaultRedisScript<>();
        ClassPathResource resource = new ClassPathResource(LUA_SCRIPT_PUT_IF_ABSENT_OR_GET_OLD__PATH);
        redisScript.setScriptSource(new ResourceScriptSource(resource));
        redisScript.setResultType(String.class);
        String keyStr = "", valueStr = "";
        try{
            keyStr = (String) key;
            valueStr = (String) value;
        }catch (ClassCastException e){
            keyStr = String.valueOf(key);
            valueStr = String.valueOf(value);
        }

        long millis = timeUnit.toMillis(expireTime);
        String result = cache.execute(redisScript, List.of(keyStr), valueStr, String.valueOf(millis));
        return (V) result;
    }

    @Override
    public void clear() {

    }


    @Override
    public boolean containsKey(K key) {
        return false;
    }

    @Override
    public void remove(K key) {

    }
}
