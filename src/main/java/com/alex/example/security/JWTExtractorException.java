package com.alex.example.security;

public class JWTExtractorException extends Exception {

    public JWTExtractorException(String s) {
        super(s);
    }

    public JWTExtractorException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public JWTExtractorException(Throwable throwable) {
        super(throwable);
    }
}
