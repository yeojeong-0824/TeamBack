package com.example.demo.security.refreshToken;

import com.example.demo.config.redis.RedisRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void save(RefreshToken data) {
        refreshTokenRepository.save(data);
    }

    @Override
    public RefreshToken findById(String token) {
        Optional<RefreshToken> savedOptionalRefreshToken = refreshTokenRepository.findById(token);
        if(savedOptionalRefreshToken.isEmpty()) return null;

        RefreshToken savedRefreshToken = savedOptionalRefreshToken.get();
        savedRefreshToken.counting();

        if(savedRefreshToken.getCount() < 0) {
            this.deleteById(token);
            return null;
        }

        refreshTokenRepository.save(savedRefreshToken);
        return savedRefreshToken;
    }

    @Override
    public void updateById(RefreshToken data, String id) {}

    @Override
    public void deleteById(String token) {
        refreshTokenRepository.deleteById(token);
    }

    @Override
    public Page<RefreshToken> findAll() {
        return null;
    }
}
