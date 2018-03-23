package com.william.devx.core.auth;


import com.william.devx.Devx;
import com.william.devx.core.dto.OptInfo;

import java.util.Optional;

public interface AuthAdapter {

    default <E extends OptInfo> Optional<E> getOptInfo() {
        return Devx.context().optInfo();
    }

    <E extends OptInfo> Optional<E> getOptInfo(String token);

    default void removeOptInfo() {
        Optional<OptInfo> tokenInfoOpt = getOptInfo();
        if (tokenInfoOpt.isPresent()) {
            removeOptInfo(tokenInfoOpt.get().getToken());
        }
    }

    void removeOptInfo(String token);

    <E extends OptInfo> void setOptInfo(E optInfo);
}
