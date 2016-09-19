package com.shedhack.requestbody.cache.filter;

public class RequestBodyThreadLocal {

    private static final ThreadLocal<String> local = new ThreadLocal<>();

    public static void set(String requestId) {
        local.set(requestId);
    }

    public static String get() {
        return local.get();
    }

    public static void clear(){
        local.remove();
    }

}
