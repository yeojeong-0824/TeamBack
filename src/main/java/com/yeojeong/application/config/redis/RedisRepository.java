package com.yeojeong.application.config.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class RedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    public Long incrementViewCount(Long key) {
        String redisKey = String.valueOf(key);
        return redisTemplate.opsForValue().increment(redisKey);
    }

    public void deleteViewCount(Long id) {
        String key = "viewCount:" + id;
        redisTemplate.delete(key);
    }

    public void restViewCount(Long key) {
        String redisKey = String.valueOf(key);
        redisTemplate.delete(redisKey);
    }

    public Boolean lock(Long key) {
        return redisTemplate
                .opsForValue()
                .setIfAbsent(generateKey(key), "lock", Duration.ofMillis(3_000));
    }

    public Boolean unlock(Long key) {
        return redisTemplate.delete(generateKey(key));
    }

    private String generateKey(Long key) {
        return key.toString();
    }
}
