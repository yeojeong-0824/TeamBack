package com.yeojeong.application.config.exception;

import com.yeojeong.application.config.exception.handler.ErrorCode;
import lombok.Getter;

// 서버에 문제가 생겼을 때를 위한 SercerException을 만들었습니다.
// 해당 예외는 개발 실수로 생기는 예외들로 구성하면 좋을 것 같습니다.
@Getter
public class ServerException extends RuntimeException{
    ErrorCode errorCode;
    public ServerException(ErrorCode errorCode) {

        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
