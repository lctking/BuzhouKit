package com.lctking.buzhoukitidempotent.exception;

public class IdempotentException extends RuntimeException{
    public IdempotentException(String message){
        super(message);
    }
}
