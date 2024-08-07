package com.example.demo.config.redis;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


// Redis 사용시 다른 도메인에서 중복된 값 입력 같은 문제가 발생할 것 같아 식별자를 생성했습니다!
@Getter
@RequiredArgsConstructor
public enum RedisIdentifier {
    EMAIL("EMAIL:");

    private final String identifier;
}
