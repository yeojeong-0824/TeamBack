package com.yeojeong.application.security.config.refreshtoken.application.implement;

import com.yeojeong.application.config.exception.AuthedException;
import com.yeojeong.application.security.config.refreshtoken.domain.RefreshToken;
import com.yeojeong.application.security.config.refreshtoken.domain.RefreshTokenRepository;
import com.yeojeong.application.security.config.refreshtoken.application.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        return refreshTokenRepository.findById(token)
                .orElseThrow(() -> new AuthedException("Refresh Token의 값을 찾을 수 없습니다."));
    }

    @Override
    public void delete(RefreshToken token) {
        refreshTokenRepository.delete(token);
    }
}
