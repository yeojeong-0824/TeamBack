package com.yeojeong.application.config.exception;

public class AuthedException extends RuntimeException {
    public AuthedException(String massage) {
        super(massage);
    }
}
