package com.yeojeong.application.config.exception;

import com.yeojeong.application.config.exception.handler.ErrorCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse {
    private final String dateTime = LocalDateTime.now().toString();
    private final int statusCode;
    private final String error;
    private final String message;

    public ErrorResponse(ErrorCode errorCode) {
        this.statusCode = errorCode.getHttpStatus().value();
        this.error = errorCode.getHttpStatus().name();
        this.message = errorCode.getMessage();
    }
}
