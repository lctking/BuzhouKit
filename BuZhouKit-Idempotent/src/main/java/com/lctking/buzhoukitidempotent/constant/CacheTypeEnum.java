package com.lctking.buzhoukitidempotent.constant;

public enum CacheTypeEnum {
    REDIS,
    CAFFEINE,
    BLOOM,

    /**
     * 本地则选用caffeine
     */
    LOCAL
}
