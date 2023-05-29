package com.infisical.secretops.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.infisical.secretops.exception.InfisicalException;
import com.infisical.secretops.exception.InitException;
import com.infisical.secretops.http.APIClient;
import com.infisical.secretops.http.APIResponse;
import com.infisical.secretops.mapper.SecretDtoToSecretMapper;
import com.infisical.secretops.model.Secret;
import com.infisical.secretops.model.SecretDto;
import com.infisical.secretops.model.apiresponse.WorkspaceConfig;
import com.infisical.secretops.model.crypt.DecryptInput;
import com.infisical.secretops.util.CryptUtil;
import com.infisical.secretops.util.ObjectMapperUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SecretService {

    private APIClient apiClient;
    private long ttlSeconds;
    private String serviceTokenKey;
    private WorkspaceConfig workspaceConfig;
    private SecretDtoToSecretMapper secretDtoToSecretMapper;

    public SecretService(APIClient client, Long ttlSeconds, String serviceTokenKey) {
        this.apiClient = client;
        this.ttlSeconds = ttlSeconds;
        this.serviceTokenKey = serviceTokenKey;
        this.secretDtoToSecretMapper = new SecretDtoToSecretMapper();
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
                List<SecretDto> secretDtos = ObjectMapperUtil.getMapper()
                        .readValue(response.getResponseBody(), new TypeReference<List<SecretDto>>(){});
                return secretDtos.stream()
                        .map(dto -> secretDtoToSecretMapper.apply(dto, workspaceConfig))
                        .collect(Collectors.toList());
            }
            throw new InfisicalException("Error while fetching all secrets" + response);
        } catch (IOException e) {
            throw new InfisicalException("Error while fetching all secrets", e);
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
}
