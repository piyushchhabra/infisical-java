package com.infisical.secretops.util;

import com.infisical.secretops.model.crypt.DecryptInput;
import com.infisical.secretops.model.crypt.EncryptInput;
import com.infisical.secretops.model.crypt.EncryptOutput;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ArrayUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@UtilityClass
public class CryptUtil {

    public EncryptOutput encrypt128BitHexKey(EncryptInput input) {
        return null;
    }

    public String decrypt128BitHexKey(DecryptInput input) throws Exception{
        try {
            String key = input.getKey();
            String tag = input.getTag();
            String iv = input.getIv();
            String ciphertext = input.getCipherText();

            String algorithm = "AES/GCM/NoPadding";
            byte[] ivBytes = Base64.getDecoder().decode(iv);
            byte[] keyBytes = key.getBytes("UTF-8");

            Cipher cipher = Cipher.getInstance(algorithm);
            SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, ivBytes);

            cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec);

            byte[] ciphertextBytes = Base64.getDecoder().decode(ciphertext);
            byte[] tagBytes = Base64.getDecoder().decode(tag);

            ciphertextBytes = ArrayUtils.addAll(ciphertextBytes, tagBytes);
            byte[] decryptedBytes = cipher.doFinal(ciphertextBytes);

            return new String(decryptedBytes, "UTF-8");
        } catch (Exception e) {
            System.out.println("Error in decryption: " + e);
            throw e;
        }
    }

}
