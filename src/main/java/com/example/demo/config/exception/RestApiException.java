package com.example.demo.config.exception;

import com.example.demo.config.exception.handler.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class RestApiException extends RuntimeException{
    ErrorCode errorCode;
    public RestApiException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
