package com.infisical.secretops.mapper;

import com.infisical.secretops.model.Secret;
import com.infisical.secretops.model.SecretDto;
import com.infisical.secretops.model.apiresponse.WorkspaceConfig;
import com.infisical.secretops.model.crypt.DecryptInput;
import com.infisical.secretops.util.CryptUtil;

import java.util.function.BiFunction;

public class SecretDtoToSecretMapper implements BiFunction<SecretDto, WorkspaceConfig, Secret> {
    @Override
    public Secret apply(SecretDto secretDto, WorkspaceConfig config) {
        String secretName = CryptUtil.decrypt128BitHexKey(DecryptInput.builder()
                .key(config.getWorkspaceKey())
                .iv(secretDto.getSecretKeyIV())
                .tag(secretDto.getSecretKeyTag())
                .cipherText(secretDto.getSecretKeyCiphertext())
                .build());
        String secretValue = CryptUtil.decrypt128BitHexKey(DecryptInput.builder()
                .key(config.getWorkspaceKey())
                .iv(secretDto.getSecretValueCiphertext())
                .tag(secretDto.getSecretValueTag())
                .cipherText(secretDto.getSecretValueCiphertext())
                .build());
        return Secret.builder()
                .secretName(secretName)
                .secretValue(secretValue)
                .environment(secretDto.getEnvironment())
                .workspace(secretDto.getWorkspace())
                .version(secretDto.getVersion())
                .type(secretDto.getType())
                .createdAt(secretDto.getCreatedAt())
                .updatedAt(secretDto.getUpdatedAt())
                .isFallback(false)
                .type(secretDto.getType())
                .lastFetchedAt(System.currentTimeMillis())
                .build();
    }
}
