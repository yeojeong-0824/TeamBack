package com.yeojeong.application.config.exception;

import com.yeojeong.application.config.exception.handler.ErrorCode;
import lombok.Getter;

@Getter
public class RestApiException extends RuntimeException{
    ErrorCode errorCode;
    public RestApiException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
