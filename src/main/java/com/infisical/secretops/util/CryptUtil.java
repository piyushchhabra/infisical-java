package com.infisical.secretops.util;

import com.infisical.secretops.model.crypt.DecryptInput;
import com.infisical.secretops.model.crypt.EncryptInput;
import com.infisical.secretops.model.crypt.EncryptOutput;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ArrayUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

@UtilityClass
public class CryptUtil {

    public EncryptOutput encrypt128BitHexKey(EncryptInput input) throws Exception {
        String plainText = input.getPlainText();
        String key = input.getKey();

        String algorithm = "AES/GCM/NoPadding";

        byte[] ivBytes = generateIV(16);
        byte[] keyBytes = key.getBytes("UTF-8");

        Cipher cipher = Cipher.getInstance(algorithm);
        SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");

        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, ivBytes);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmParameterSpec);

        byte[] ciphertextBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        byte[] tagBytes = substring(ciphertextBytes, ciphertextBytes.length - 16, ciphertextBytes.length);
        String ivString = Base64.getEncoder().encodeToString(ivBytes);
        String tagStr = Base64.getEncoder().encodeToString(tagBytes);
        String cipherTextStr = Base64.getEncoder().encodeToString(substring(ciphertextBytes, 0, ciphertextBytes.length - 16));

        return EncryptOutput.builder().cipherText(cipherTextStr).iv(ivString).tag(tagStr).build();
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
            // TODO: add logs
            throw e;
        }
    }

    @SneakyThrows
    public EncryptOutput encryptSymmetric(EncryptInput input) {
        String plainText = input.getPlainText();
        String key = input.getKey();

        String algorithm = "AES/GCM/NoPadding";

        byte[] ivBytes = generateIV(12);

        Cipher cipher = Cipher.getInstance(algorithm);

        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, ivBytes);
        cipher.init(Cipher.ENCRYPT_MODE, getKeySpec(key), gcmParameterSpec);

        byte[] ciphertextBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        byte[] tagBytes = substring(ciphertextBytes, ciphertextBytes.length - 16, ciphertextBytes.length);
        String ivString = Base64.getEncoder().encodeToString(ivBytes);
        String tagStr = Base64.getEncoder().encodeToString(tagBytes);
        String cipherTextStr = Base64.getEncoder().encodeToString(substring(ciphertextBytes, 0, ciphertextBytes.length - 16));

        return EncryptOutput.builder().cipherText(cipherTextStr).iv(ivString).tag(tagStr).build();
    }

    @SneakyThrows
    public String decryptSymmetric(DecryptInput input) {
        String tag = input.getTag();
        String iv = input.getIv();
        String ciphertext = input.getCipherText();

        String algorithm = "AES/GCM/NoPadding";
        byte[] ivBytes = Base64.getDecoder().decode(iv);

        Cipher cipher = Cipher.getInstance(algorithm);
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, ivBytes);

        cipher.init(Cipher.DECRYPT_MODE, getKeySpec(input.getKey()), gcmParameterSpec);

        byte[] ciphertextBytes = Base64.getDecoder().decode(ciphertext);
        byte[] tagBytes = Base64.getDecoder().decode(tag);

        ciphertextBytes = ArrayUtils.addAll(ciphertextBytes, tagBytes);
        byte[] decryptedBytes = cipher.doFinal(ciphertextBytes);

        return new String(decryptedBytes, "UTF-8");
    }

    public String createSymmetricKey() {
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[32];
        random.nextBytes(keyBytes);
        return Base64.getEncoder().encodeToString(keyBytes);
    }

    @SneakyThrows
    private SecretKeySpec getKeySpec(final String myKey) {
        MessageDigest sha;
        byte[] key = myKey.getBytes("UTF-8");
        sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16);
        return new SecretKeySpec(key, "AES");
    }

    private static byte[] generateIV(int length) {
        SecureRandom random = new SecureRandom();
        byte[] iv = new byte[length];
        random.nextBytes(iv);
        return iv;
    }

    private static byte[] substring(byte[] array, int start, int end) {
        if (end <= start)
            return null;
        int length = (end - start);

        byte[] newArray = new byte[length];
        System.arraycopy(array, start, newArray, 0, length);
        return newArray;
    }

}
