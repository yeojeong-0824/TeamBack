package com.example.demo.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class RedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public void setData(String key, Object value, Duration time) {
        ValueOperations<String, Object> redisValueOperations = redisTemplate.opsForValue();
        redisValueOperations.set(key, value, time);
    }

    public void setData(String key, Object value) {
        ValueOperations<String, Object> redisValueOperations = redisTemplate.opsForValue();
        redisValueOperations.set(key, value);
    }

    public Object getDataByKey(String key) {
        ValueOperations<String, Object> redisValueOperations = redisTemplate.opsForValue();
        return redisValueOperations.get(key);
    }

    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    public Long incrementViewCount(Long key) {
        String redisKey = String.valueOf(key);
        return redisTemplate.opsForValue().increment(redisKey);
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
