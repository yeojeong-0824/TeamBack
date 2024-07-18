package com.example.demo.config.exception;


// 서버에 문제가 생겼을 때를 위한 SercerException을 만들었습니다.
// 해당 예외는 개발 실수로 생기는 예외들로 구성하면 좋을 것 같습니다.
public class ServerException extends RuntimeException{
    public ServerException(String message) {
        super(message);
    }
}
