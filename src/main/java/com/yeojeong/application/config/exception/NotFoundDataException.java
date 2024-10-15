package com.yeojeong.application.config.exception;

import com.yeojeong.application.config.exception.handler.ErrorCode;
import lombok.Getter;

@Getter
public class NotFoundDataException extends RuntimeException {
    ErrorCode errorCode;
    public NotFoundDataException(ErrorCode errorCode) {

        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
