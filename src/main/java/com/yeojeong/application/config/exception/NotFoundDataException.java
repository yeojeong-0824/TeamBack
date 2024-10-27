package com.yeojeong.application.config.exception;

import lombok.Getter;

@Getter
public class NotFoundDataException extends RuntimeException {
    public NotFoundDataException(String message) {
        super(message);
    }
}
