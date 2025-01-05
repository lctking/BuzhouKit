package com.lctking.buzhoukitcore.cache;

import com.lctking.buzhoukitcore.cache.impl.CaffeineCacheServiceImpl;
import com.lctking.buzhoukitcore.cache.service.CacheService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CaffeineCacheServiceTest {
    private CacheService<String, Object> caffeineCache;
    private List<String> randomStrings;

    private final String randomStringFormat = "caffeine_test_random_str_%s";

    @BeforeEach
    void initial(){
//        caffeineCache = new CaffeineCacheServiceImpl<>(10,1024,20, TimeUnit.SECONDS,false);
//        randomStrings = new ArrayList<>(1000);
//        for(int i = 0; i < 1000; ++i){
//            randomStrings.add(String.format(randomStringFormat, UUID.randomUUID()));
//        }
    }

    @Test
    void test() throws InterruptedException {
//        String k = "k";
//        String v = "v";
//        // test put,remove and containsKey
//        caffeineCache.put(k,v);
//        Assertions.assertTrue(caffeineCache.containsKey(k));
//        caffeineCache.remove(k);
//        Assertions.assertFalse(caffeineCache.containsKey((k)));
//
//
//
//        // put into cache
//        for(String s : randomStrings){
//            caffeineCache.put(s,s);
//        }
//        // test get
//        for(String s : randomStrings){
//            Assertions.assertEquals(caffeineCache.get(s),s);
//        }
//
//        // sleep 2.0s
//        Thread.sleep(20000L);
//        // test expire
//        for(String s : randomStrings){
//            Assertions.assertFalse(caffeineCache.containsKey(s));
//        }




    }
}
