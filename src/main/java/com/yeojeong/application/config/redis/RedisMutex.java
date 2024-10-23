package com.yeojeong.application.config.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisMutex {
    private final RedisTemplate<String, String> redisTemplate;

    public boolean lock(String key, long timeout) {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(key, "locked", timeout, TimeUnit.SECONDS);
        return result != null && result;
    }

    public void unlock(String key) {
        redisTemplate.delete(key);
    }
}
