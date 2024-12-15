package com.yeojeong.application.domain.member.application.redisauthedservice;

import com.yeojeong.application.domain.member.domain.RedisAuthed;

public interface RedisAuthedService {
    void save(RedisAuthed redisAuthed);
    RedisAuthed findById(String id);
    boolean checkKey(String id, String authKey);
    void delete(String id);
}
