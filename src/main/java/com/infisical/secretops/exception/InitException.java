package com.infisical.secretops.exception;

public class InitException extends RuntimeException{

    public InitException(String message) {
        super("Can not initialize Infisical Client: " + message);
    }
}
