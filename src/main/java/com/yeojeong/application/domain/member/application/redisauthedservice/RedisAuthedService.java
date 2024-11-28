package com.yeojeong.application.domain.member.application.redisauthedservice;

import com.yeojeong.application.domain.member.domain.RedisAuthed;

public interface RedisAuthedService {
    void save(RedisAuthed redisAuthed);
    RedisAuthed findById(String email);
    boolean checkKey(String email, String authKey);
    void delete(String email);
}
