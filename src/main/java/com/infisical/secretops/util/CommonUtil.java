package com.infisical.secretops.util;

import com.infisical.secretops.model.Secret;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CommonUtil {

    public Secret getFallbackSecret(String name) {
        return Secret.builder()
                .secretName(name)
                .isFallback(true)
                .lastFetchedAt(System.currentTimeMillis())
                .build();
    }
}
