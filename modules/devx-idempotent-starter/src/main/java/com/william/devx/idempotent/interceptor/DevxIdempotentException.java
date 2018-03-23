package com.william.devx.idempotent.interceptor;

public class DevxIdempotentException extends RuntimeException {

    public DevxIdempotentException(String message) {
        super(message);
    }
}
