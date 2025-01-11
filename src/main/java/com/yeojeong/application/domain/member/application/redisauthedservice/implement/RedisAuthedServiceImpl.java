package com.yeojeong.application.domain.member.application.redisauthedservice.implement;

import com.yeojeong.application.config.exception.AuthedException;
import com.yeojeong.application.domain.member.application.redisauthedservice.RedisAuthedService;
import com.yeojeong.application.domain.member.domain.RedisAuthed;
import com.yeojeong.application.domain.member.domain.RedisAuthedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisAuthedServiceImpl implements RedisAuthedService {
    private final RedisAuthedRepository redisAuthedRepository;

    public void save(RedisAuthed redisAuthed) {
        redisAuthedRepository.save(redisAuthed);
    }

    public RedisAuthed findById(String id) {
        return redisAuthedRepository.findById(id).orElseThrow(() -> new AuthedException("정보를 찾을 수 없습니다."));
    }

    public boolean checkKey(String id, String authKey) {
        Optional<RedisAuthed> optionalEntity = redisAuthedRepository.findById(id);
        if(optionalEntity.isEmpty()) return false;
        RedisAuthed entity = optionalEntity.get();
        return entity.getValue().equals(authKey);
    }

    public void delete(String id) {
        redisAuthedRepository.deleteById(id);
    }
}
