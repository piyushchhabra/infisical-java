package com.infisical.secretops.model.crypt;

import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DecryptInput {
    String cipherText;
    String key;
    String iv;
    String tag;
}
