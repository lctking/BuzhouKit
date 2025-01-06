package com.lctking.test.IdempotentTest.exception;

public class CustomizeException extends RuntimeException{
    public CustomizeException(String message){
        super(message);
    }
}
