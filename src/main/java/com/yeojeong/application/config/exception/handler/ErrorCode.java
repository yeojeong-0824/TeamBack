package com.yeojeong.application.config.exception.handler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    USER_MISMATCH(HttpStatus.FORBIDDEN, "작성자와 로그인한 사용자가 일치하지 않습니다."),

    // JWT 관련
    UNAUTHORIZED_CLIENT(HttpStatus.BAD_REQUEST, "접근 토큰이 없습니다."),
    FORBIDDEN_CLIENT(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "유효기간이 만료된 토큰입니다."),
    JWT_DECODE_FAIL(HttpStatus.UNAUTHORIZED, "JWT Token 변환이 실패하였습니다. 올바른 토큰이 필요합니다."),
    JWT_SIGNATURE_FAIL(HttpStatus.UNAUTHORIZED, "JWT Token 값이 잘못되었습니다. 올바른 토큰이 필요합니다."),

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Invalid request"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "잘못된 요청입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
