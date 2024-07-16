package com.example.demo.security.refreshToken;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public void addRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findRefreshToken(String token) {
        Optional<RefreshToken> savedOptionalRefreshToken = refreshTokenRepository.findById(token);
        if(savedOptionalRefreshToken.isEmpty()) return Optional.empty();

        RefreshToken savedRefreshToken = savedOptionalRefreshToken.get();
        savedRefreshToken.counting();
        refreshTokenRepository.save(savedRefreshToken);

        return savedOptionalRefreshToken;
    }

    public void dropRefreshToken(String token) {
        refreshTokenRepository.deleteById(token);
    }

    //만료된 토큰 삭제
//    private void dropExpirationToken() {
//        List<RefreshToken> refreshTokenList = refreshTokenRepository.findAll();
//    }
}
