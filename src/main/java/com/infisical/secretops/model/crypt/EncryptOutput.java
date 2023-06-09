package com.infisical.secretops.model.crypt;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EncryptOutput {
    String iv;
    String tag;
    String cipherText;
}
