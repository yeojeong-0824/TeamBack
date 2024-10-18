package com.yeojeong.application.security.filter.exception;

import com.yeojeong.application.config.exception.handler.ErrorCode;

public class FilterException extends RuntimeException {
    ErrorCode errorCode;
    public FilterException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
