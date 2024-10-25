package com.yeojeong.application.config.exception;

import lombok.Getter;

@Getter
public class DuplicatedException extends RuntimeException {
    public DuplicatedException(String message) {
        super(message);
    }
}
