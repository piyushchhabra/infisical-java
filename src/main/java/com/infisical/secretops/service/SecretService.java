package com.infisical.secretops.service;

import com.infisical.secretops.exception.InfisicalException;
import com.infisical.secretops.exception.InitException;
import com.infisical.secretops.exception.SecretNotFoundException;
import com.infisical.secretops.http.APIClient;
import com.infisical.secretops.http.APIResponse;
import com.infisical.secretops.mapper.SecretDtoToSecretMapper;
import com.infisical.secretops.model.Secret;
import com.infisical.secretops.model.internal.SecretDtoListResponse;
import com.infisical.secretops.model.internal.SecretDtoResponse;
import com.infisical.secretops.model.apiresponse.WorkspaceConfig;
import com.infisical.secretops.model.crypt.DecryptInput;
import com.infisical.secretops.model.crypt.EncryptInput;
import com.infisical.secretops.model.crypt.EncryptOutput;
import com.infisical.secretops.model.options.CreateOptions;
import com.infisical.secretops.model.options.DeleteOptions;
import com.infisical.secretops.model.options.GetOptions;
import com.infisical.secretops.util.CommonUtil;
import com.infisical.secretops.util.CryptUtil;
import com.infisical.secretops.util.ObjectMapperUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

//TODO: Add logs for success and failure errors

public class SecretService {

    private final APIClient apiClient;
    private final long ttlSeconds;
    private final String serviceTokenKey;
    private final WorkspaceConfig workspaceConfig;
    private final SecretDtoToSecretMapper secretDtoToSecretMapper;

    private Map<String, Secret> cachedSecrets;

    public SecretService(APIClient client, Long ttlSeconds, String serviceTokenKey) {
        this.apiClient = client;
        this.ttlSeconds = ttlSeconds;
        this.serviceTokenKey = serviceTokenKey;
        this.secretDtoToSecretMapper = new SecretDtoToSecretMapper();
        this.cachedSecrets = new HashMap<>();
        try {
            this.workspaceConfig = getWorkspaceConfig();
        } catch (Exception e) {
            throw new InitException("Could not fetch workspace config | " + e.getMessage());
        }
    }

    public List<Secret> getAllSecrets() {
        String path = "/api/v3/secrets";
        Map<String, String> params = new HashMap<>();
        params.put("workspaceId", workspaceConfig.getWorkspaceId());
        params.put("environment", workspaceConfig.getEnvironment());
        try {
            APIResponse response = apiClient.doGetRequest(path, params);
            if (response.isSuccess()) {
                SecretDtoListResponse dtoList = ObjectMapperUtil.getMapper().readValue(response.getResponseBody(), SecretDtoListResponse.class);
                List<Secret> secretList = dtoList.getSecrets().stream()
                        .map(dto -> secretDtoToSecretMapper.apply(dto, workspaceConfig))
                        .collect(Collectors.toList());
                secretList.forEach(secret -> cachedSecrets.put(secret.getType()+"-"+secret.getSecretName(), secret));
                return secretList;
            }
            throw new InfisicalException("Error while fetching all secrets" + response);
        } catch (Exception e) {
            throw new InfisicalException("Error while fetching all secrets", e);
        }
    }

    public Secret getSecret(String secretName, GetOptions options) {
        String cacheKey = options.getType() + "-" + secretName;
        Secret cachedSecret = cachedSecrets.get(cacheKey);
        if (cachedSecret != null) {
            long currentTime = System.currentTimeMillis();
            long lastFetchedAt = cachedSecret.getLastFetchedAt();
            if ((lastFetchedAt + (ttlSeconds*1000)) > currentTime) {
                //TODO: add logs
                return cachedSecret;
            }
        }

        String path = "/api/v3/secrets/" + secretName;
        Map<String, String> params = new HashMap<>();
        params.put("workspaceId", workspaceConfig.getWorkspaceId());
        params.put("environment", workspaceConfig.getEnvironment());
        params.put("type", options.getType());

        try {
            APIResponse response = apiClient.doGetRequest(path, params);
            if (response.isSuccess()) {
                SecretDtoResponse dtoResponse = ObjectMapperUtil.getMapper().readValue(response.getResponseBody(), SecretDtoResponse.class);
                dtoResponse.getSecret().setDecryptedSecretName(secretName);
                Secret secret = secretDtoToSecretMapper.apply(dtoResponse.getSecret(), workspaceConfig);
                cachedSecrets.put(cacheKey, secret);
                return secret;
            } else if (cachedSecret != null) {
                //TODO: log that because of error we are returning cached response
                return cachedSecret;
            }
            throw runtimeException(response, secretName, "fetch");
        } catch (Exception e) {
            if (cachedSecret != null) {
                return cachedSecret;
            }
            throw new InfisicalException("Error while fetching secret=" + secretName, e);
        }
    }

    public Secret createSecret(String secretName, String secretValue, CreateOptions options) {
        EncryptInput encryptSecretNameInput = EncryptInput.builder()
                .plainText(secretName)
                .key(workspaceConfig.getWorkspaceKey())
                .build();
        EncryptInput encryptSecretValueInput = EncryptInput.builder()
                .plainText(secretValue)
                .key(workspaceConfig.getWorkspaceKey())
                .build();
        try {
            EncryptOutput encryptSecretNameOutput = CryptUtil.encrypt128BitHexKey(encryptSecretNameInput);
            EncryptOutput encryptSecretValueOutput = CryptUtil.encrypt128BitHexKey(encryptSecretValueInput);
            String path = "/api/v3/secrets/" + secretName;

            Map<String, Object> body = new HashMap<>();
            body.put("workspaceId", workspaceConfig.getWorkspaceId());
            body.put("environment", workspaceConfig.getEnvironment());
            body.put("type", options.getType());
            body.put("secretKeyCiphertext", encryptSecretNameOutput.getCipherText());
            body.put("secretKeyIV", encryptSecretNameOutput.getIv());
            body.put("secretKeyTag", encryptSecretNameOutput.getTag());
            body.put("secretValueCiphertext", encryptSecretValueOutput.getCipherText());
            body.put("secretValueIV", encryptSecretValueOutput.getIv());
            body.put("secretValueTag", encryptSecretValueOutput.getTag());

            APIResponse response = apiClient.doPostRequest(path, null, body);
            if (response.isSuccess()) {
                SecretDtoResponse dtoResponse = ObjectMapperUtil.getMapper().readValue(response.getResponseBody(), SecretDtoResponse.class);
                dtoResponse.getSecret().setDecryptedSecretName(secretName);
                Secret secret = secretDtoToSecretMapper.apply(dtoResponse.getSecret(), workspaceConfig);
                String cacheKey = options.getType() + "-" + secretName;
                cachedSecrets.put(cacheKey, secret);
                return secret;
            }
            return CommonUtil.getFallbackSecret(secretName);
        } catch (Exception e) {
            throw new InfisicalException("Error while creating secret=" + secretName, e);
        }
    }

    public Secret deleteSecret(String secretName, DeleteOptions options) {
        String path = "/api/v3/secrets/" + secretName;
        Map<String, Object> body = new HashMap<>();
        body.put("workspaceId", workspaceConfig.getWorkspaceId());
        body.put("environment", workspaceConfig.getEnvironment());
        body.put("type", options.getType());
        try {
            APIResponse response = apiClient.doDeleteRequest(path, null, body);
            if (response.isSuccess()) {
                SecretDtoResponse dtoResponse = ObjectMapperUtil.getMapper().readValue(response.getResponseBody(), SecretDtoResponse.class);
                dtoResponse.getSecret().setDecryptedSecretName(secretName);
                Secret secret =  secretDtoToSecretMapper.apply(dtoResponse.getSecret(), workspaceConfig);
                cachedSecrets.remove(secretName);
                cachedSecrets.remove(options.getType() + "-" + secretName);
                return secret;
            }
            throw runtimeException(response, secretName, "delete");
        } catch (Exception e) {
            throw new InfisicalException("Error while deleting secret=" + secretName, e);
        }
    }


    private WorkspaceConfig getWorkspaceConfig() throws Exception {
        String path = "/api/v2/service-token";
        APIResponse response = apiClient.doGetRequest(path, null);
        if (response.isSuccess()) {
            Map<String, Object> map = ObjectMapperUtil.getMapper().readValue(response.getResponseBody(), Map.class);
            String workspaceKey = CryptUtil.decrypt128BitHexKey(DecryptInput.builder()
                    .key(serviceTokenKey)
                    .iv((String) map.get("iv"))
                    .tag((String) map.get("tag"))
                    .cipherText((String) map.get("encryptedKey"))
                    .build());
            return WorkspaceConfig.builder()
                    .workspaceId((String) map.get("workspace"))
                    .environment((String) map.get("environment"))
                    .workspaceKey(workspaceKey)
                    .build();
        }
        throw new Exception("Failed while fetching workspace config. ApiResponse: " + response);
    }

    private RuntimeException runtimeException(APIResponse response, String secretName, String operation) {
        if (Objects.nonNull(response) && response.getResponseCode() == 404) {
            return new SecretNotFoundException(secretName);
        }
        String err = "Error while operation" + operation;
        if (secretName != null) {
            err += " | secretName=" + secretName;
        }
        if (operation != null) {
            err += " | operation=" + operation;
        }
        err += " | APIResponse=" + response;
        return new InfisicalException(err);
    }
}
