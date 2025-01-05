package com.lctking.buzhoukitidempotent.utils;

import java.lang.reflect.Constructor;

public class ExceptionThrower {
    public static void throwException(Class<? extends Throwable> exceptionClass, String message) throws Throwable {
        Constructor<? extends Throwable> constructor = null;
        try {
            constructor = exceptionClass.getConstructor(String.class);
            throw constructor.newInstance(message);
        } catch (NoSuchMethodException e) {
            constructor = exceptionClass.getDeclaredConstructor();
        }
        throw constructor.newInstance();
    }
}
