package com.lctking.buzhoukitidempotent.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = BloomFilterCacheProperties.PREFIX)
public class BloomFilterCacheProperties {
    public static final String PREFIX = "idempotent.cache.redis.bloom-filter";

    private String name = "default_bloom_filter";

    /**
     * 默认-预计插入元素数量
     */
    private Long expectedInsertions = 512L;

    /**
     * 默认-误判率
     */
    private Double falseProbability = 0.001D;

    private Boolean enabled = false;
}