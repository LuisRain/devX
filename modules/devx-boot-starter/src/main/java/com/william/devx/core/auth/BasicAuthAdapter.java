package com.william.devx.core.auth;

import com.william.devx.Devx;
import com.william.devx.common.$;
import com.william.devx.core.DevxContext;
import com.william.devx.core.dto.OptInfo;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BasicAuthAdapter implements AuthAdapter {

    // token存储key
    private static final String TOKEN_INFO_FLAG = "devx:auth:token:info:";


    // Token Id 关联 key : dew:auth:token:id:rel:<code> value : <token Id>
    private static final String TOKEN_ID_REL_FLAG = "devx:auth:token:id:rel:";

    @Override
    public <E extends OptInfo> Optional<E> getOptInfo(String token) {
        String optInfoStr = Devx.cluster.cache.get(TOKEN_INFO_FLAG + token);
        if (optInfoStr != null && !optInfoStr.isEmpty()) {
            return Optional.of($.json.toObject(optInfoStr, DevxContext.getOptInfoClazz()));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void removeOptInfo(String token) {
        Optional<OptInfo> tokenInfoOpt = getOptInfo(token);
        if (tokenInfoOpt.isPresent()) {
            Devx.cluster.cache.del(TOKEN_ID_REL_FLAG + tokenInfoOpt.get().getAccountCode());
            Devx.cluster.cache.del(TOKEN_INFO_FLAG + token);
        }
    }

    @Override
    public <E extends OptInfo> void setOptInfo(E optInfo) {
        Devx.cluster.cache.del(TOKEN_INFO_FLAG + Devx.cluster.cache.get(TOKEN_ID_REL_FLAG + optInfo.getAccountCode()));
        Devx.cluster.cache.set(TOKEN_ID_REL_FLAG + optInfo.getAccountCode(), optInfo.getToken());
        Devx.cluster.cache.set(TOKEN_INFO_FLAG + optInfo.getToken(), $.json.toJsonString(optInfo));
    }

}
