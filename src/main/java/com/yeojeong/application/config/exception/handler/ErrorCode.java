package com.yeojeong.application.config.exception.handler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // user
    USER_MISMATCH(HttpStatus.FORBIDDEN, "작성자와 로그인한 사용자가 일치하지 않습니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    DUPLICATED_ID(HttpStatus.CONFLICT, "아이디가 중복되었습니다."),
    DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "닉네임이 중복되었습니다."),
    PASSWORD_NOT_ENCRYPTION(HttpStatus.BAD_GATEWAY, "비밀번호가 암호화 되지 않았습니다."),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    BLANK_ID(HttpStatus.BAD_REQUEST, "아이디가 비어있습니다."),
    BLANK_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 비어있습니다."),
    FAIL_LOGIN(HttpStatus.BAD_REQUEST, "로그인에 실패하였습니다."),
    REFRESH_TOKEN_NOT_VALID(HttpStatus.BAD_REQUEST, "Refresh Token 이 유효하지 않습니다."),

    // board
    NOT_FOUND_BOARD(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),

    // comment
    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),

    // email
    NOT_FOUND_EMAIL(HttpStatus.NOT_FOUND, "이메일을 찾을 수 없습니다."),
    EMAIL_MISMATCH(HttpStatus.FORBIDDEN, "작성한 이메일과 로그인한 사용자의 이메일이 일치하지 않습니다."),
    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "이메일이 중복되었습니다."),

    // sql
    SQL_INTEGRITY_VIOLATION(HttpStatus.BAD_REQUEST, "SQL 무결성 제약 조건이 위배되었습니다"),

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
