package com.example.demo.config.exception;

// 권한이 없는 접근에 대한 예외입니다!
public class AuthorityException extends RuntimeException {
    public AuthorityException(String message) { super(message); }
}
