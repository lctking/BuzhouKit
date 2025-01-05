package com.lctking.buzhoukitidempotent.exception;

public class CacheException extends RuntimeException{
    public CacheException(String message){
        super(message);
    }
}
