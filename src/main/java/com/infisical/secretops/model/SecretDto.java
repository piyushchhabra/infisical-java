package com.infisical.secretops.model;

import lombok.Data;

@Data
public class SecretDto {
    private int version;
    private String workspace;
    private String user;
    private String type;
    private String environment;
    private String secretKeyCiphertext;
    private String secretKeyIV;
    private String secretKeyTag;
    private String secretValueCiphertext;
    private String secretValueIV;
    private String secretValueTag;
    private String secretCommentCiphertext;
    private String secretCommentIV;
    private String secretCommentTag;
    private String createdAt;
    private String updatedAt;

    private String decryptedSecretName;
}
