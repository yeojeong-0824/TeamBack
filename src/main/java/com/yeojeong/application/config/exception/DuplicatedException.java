package com.yeojeong.application.config.exception;

import com.yeojeong.application.config.exception.handler.ErrorCode;
import lombok.Getter;

@Getter
public class DuplicatedException extends RuntimeException {
    ErrorCode errorCode;
    public DuplicatedException(ErrorCode errorCode) {

        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
