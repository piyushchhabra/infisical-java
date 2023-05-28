package com.infisical.secretops.client;

import com.infisical.secretops.exception.InitException;
import com.infisical.secretops.util.InfisicalConstants;
import org.apache.commons.lang3.StringUtils;

public class InfisicalClient {

    public InfisicalClient(String token) {
        this(token, InfisicalConstants.DEFAULT_CACHE_TTL_IN_SECONDS);
    }

    public InfisicalClient(String token, long cacheTTLInSeconds) {
        if (StringUtils.isBlank(token)) {
            throw new InitException("Token can not be empty");
        }
    }

}
