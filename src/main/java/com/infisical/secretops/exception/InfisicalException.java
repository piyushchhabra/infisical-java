package com.infisical.secretops.exception;

public class InfisicalException extends RuntimeException{

    public InfisicalException(String msg) {
        super(msg);
    }
    public InfisicalException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
