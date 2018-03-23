package com.william.devx.idempotent.strategy;

public interface DevxIdempotentProcessor {

    StatusEnum process(String optType, String optId, StatusEnum initStatus, long expireMs);

    boolean confirm(String optType, String optId);

    boolean cancel(String optType, String optId);

}
