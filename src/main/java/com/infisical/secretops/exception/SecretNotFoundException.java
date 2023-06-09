package com.infisical.secretops.exception;

public class SecretNotFoundException extends RuntimeException{
    public SecretNotFoundException(String secretName) {
        super("Infisical secret not found: " + secretName);
    }
}
