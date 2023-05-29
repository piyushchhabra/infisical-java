package com.infisical.secretops.client;

import com.infisical.secretops.exception.InitException;
import com.infisical.secretops.http.APIClient;
import com.infisical.secretops.model.InfisicalClientOptions;
import com.infisical.secretops.model.Secret;
import com.infisical.secretops.service.SecretService;
import com.infisical.secretops.util.InfisicalConstants;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

public class InfisicalClient {

    private String serviceToken;
    private SecretService secretService;

    public InfisicalClient(InfisicalClientOptions options) {
        if (Objects.isNull(options)) {
            throw new InitException("InfisicalClientOptions can not be null");
        }

        if (StringUtils.isBlank(options.getToken())) {
            throw new InitException("Token can not be empty");
        }

        String token = options.getToken();
        Long ttlSeconds = Objects.nonNull(options.getCacheTtlInSeconds()) ? options.getCacheTtlInSeconds() : InfisicalConstants.DEFAULT_CACHE_TTL_IN_SECONDS;
        String siteUrl = StringUtils.isNotBlank(options.getSiteURL()) ? options.getSiteURL() : InfisicalConstants.INFISICAL_URL;

        if (ttlSeconds <= 60L) {
            throw new InitException("Cache TTL can not be less than 60 seconds");
        }
        this.init(token, ttlSeconds, siteUrl);
    }

    private void init(final String token, final long ttlSeconds, final String siteUrl) {
        int lastDotIdx = token.lastIndexOf('.');
        this.serviceToken = token.substring(0, lastDotIdx);;
        String serviceTokenKey = token.substring(lastDotIdx + 1);
        this.secretService = new SecretService(new APIClient(serviceToken, siteUrl), ttlSeconds, serviceTokenKey);
    }

    public List<Secret> getAllSecrets() {
        return secretService.getAllSecrets();
    }

}
