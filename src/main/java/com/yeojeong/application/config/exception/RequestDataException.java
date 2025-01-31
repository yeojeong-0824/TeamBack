package com.yeojeong.application.config.exception;

public class RequestDataException extends RuntimeException {
    public RequestDataException(String message) {
        super(message);
    }
}