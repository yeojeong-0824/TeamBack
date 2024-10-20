package com.yeojeong.application.security.config.refreshtoken.service.implement;

import com.yeojeong.application.security.config.refreshtoken.domain.RefreshToken;
import com.yeojeong.application.security.config.refreshtoken.domain.RefreshTokenRepository;
import com.yeojeong.application.security.config.refreshtoken.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

        if(savedRefreshToken.getCount() <= 1) {
            refreshTokenRepository.deleteById(token);
            return null;
        }

        savedRefreshToken.counting();
        return savedRefreshToken;
    }
}
