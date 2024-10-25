package com.yeojeong.application.config.exception;

import lombok.Getter;

@Getter
public class RequestDataException extends RuntimeException {
    public RequestDataException(String message) {
        super(message);
    }
}