package com.example.demo.config.exception;

// 유효성 검사에는 통과되나, 서버에서 데이터를 처리하지 못할 때를 위한 예외입니다.
// 여러 곳에서 사용 될 것 같아 Global로 넣었습니다.
public class RequestDataException extends RuntimeException {
    public RequestDataException(String message) {
        super(message);
    }
}