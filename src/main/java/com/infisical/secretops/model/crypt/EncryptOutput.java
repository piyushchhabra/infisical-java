package com.infisical.secretops.model.crypt;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EncryptOutput {
    String iv;
    String tag;
    String cipherText;
}
