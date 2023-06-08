package com.infisical.secretops.util;

import com.infisical.secretops.model.crypt.DecryptInput;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CryptUtilTest {

    @Test
    @SneakyThrows
    public void testDecrypt128BitHexKey() {
        String ciphertext = "2LPVwa7s4+xyd8KF94r07TOCaOdf4X90NWqhHQCpoJGT+T3TYjKCWf3V+A==";
        String iv = "S4/yimaF8bu0jQW9uagIJA==";
        String tag = "L6BtTic18dd4fNL2maytFA==";
        String key = "password1232asdfpassword1232asdf";

        DecryptInput decryptInput = DecryptInput.builder()
                .key(key)
                .cipherText(ciphertext)
                .iv(iv)
                .tag(tag)
                .build();
        String decryptedText = CryptUtil.decrypt128BitHexKey(decryptInput);
        Assertions.assertEquals("The quick brown fox jumps over the lazy dog", decryptedText);
    }
}