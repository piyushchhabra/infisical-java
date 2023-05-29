package com.infisical.secretops.model.crypt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DecryptInput {
    String cipherText;
    String key;
    String iv;
    String tag;
}
