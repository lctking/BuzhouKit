package com.lctking.buzhoukitidempotent.constant;

public enum CacheTypeEnum {
    REDIS,
    CAFFEINE,

    /**
     * 本地则选用caffeine
     */
    LOCAL
}
