package com.lctking.buzhoukitidempotent.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = BloomFilterCacheProperties.PREFIX)
public class BloomFilterCacheProperties {
    public static final String PREFIX = "idempotent.cache.redis.bloom-filter";

    private String name = "default_bloom_filter";

    private Long expectedInsertions;

    private Double falseProbability;
}