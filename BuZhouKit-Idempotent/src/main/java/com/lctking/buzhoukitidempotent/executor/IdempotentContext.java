package com.lctking.buzhoukitidempotent.executor;

import java.util.HashMap;
import java.util.Map;

public final class IdempotentContext {
    private static final ThreadLocal<Map<String, Object>> CONTEXT = new ThreadLocal<>();

    public static void setContext(Map<String,Object> context){
        Map<String, Object> oldContext = CONTEXT.get();
        if(oldContext != null && !oldContext.isEmpty()){
            oldContext.putAll(context);
        }else {
            CONTEXT.set(context);
        }
    }

    public static Map<String, Object> getContext(){
        return CONTEXT.get();
    }

    public static void setKey(String key, Object value){
        Map<String, Object> oldContext = CONTEXT.get();
        if(oldContext != null && !oldContext.isEmpty()){
            oldContext.put(key, value);
        }else {
            HashMap<String, Object> newContext = new HashMap<>();
            newContext.put(key, value);
            CONTEXT.set(newContext);
        }
    }

    public static Object getKey(String key){
        Map<String, Object> oldContext = CONTEXT.get();
        if(oldContext == null || oldContext.isEmpty()){
            return null;
        }
        return oldContext.get(key);
    }

    public static void removeContext(){
        CONTEXT.remove();
    }

}
