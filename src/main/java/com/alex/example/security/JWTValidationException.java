package com.alex.example.security;

public class JWTValidationException extends Exception {

    public JWTValidationException(String s) {
        super(s);
    }

    public JWTValidationException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public JWTValidationException(Throwable throwable) {
        super(throwable);
    }
}
