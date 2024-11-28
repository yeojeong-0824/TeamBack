package com.yeojeong.application.domain.member.application.redisauthedservice.implement;

import com.yeojeong.application.config.exception.AuthedException;
import com.yeojeong.application.domain.member.application.redisauthedservice.RedisAuthedService;
import com.yeojeong.application.domain.member.domain.RedisAuthed;
import com.yeojeong.application.domain.member.domain.RedisAuthedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisAuthedServiceImpl implements RedisAuthedService {
    private final RedisAuthedRepository redisAuthedRepository;

    public void save(RedisAuthed redisAuthed) {
        redisAuthedRepository.save(redisAuthed);
    }

    public RedisAuthed findById(String email) {
        return redisAuthedRepository.findById(email).orElseThrow(() -> new AuthedException("정보를 찾을 수 없습니다."));
    }

    public boolean checkKey(String email, String authKey) {
        RedisAuthed entity = findById(email);
        return entity.getValue().equals(authKey);
    }

    public void delete(String email) {
        redisAuthedRepository.deleteById(email);
    }
}
