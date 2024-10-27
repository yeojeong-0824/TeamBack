package com.yeojeong.application.config.exception.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ExceptionResponse {
    private final String timestamp = LocalDateTime.now().toString();
    private final int status;
    private final String error;
    private final String path;

    public ExceptionResponse(int httpStatus, String path, String message) {
        this.status = httpStatus;
        this.path = path;
        this.error = message;
    }
}
