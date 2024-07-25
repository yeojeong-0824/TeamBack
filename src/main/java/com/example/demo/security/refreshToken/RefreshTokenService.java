package com.example.demo.security.refreshToken;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public void addRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

    //리팩토링 필요함
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

    // 만료된 토큰 정리 하루에 한번 진행
    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000)
    private void dropExpirationToken() {
        log.info("만료된 Refresh Token 정리");
        long time = System.currentTimeMillis();

        List<RefreshToken> refreshTokenList = refreshTokenRepository.findByOrderByExpirationTime();
        for(RefreshToken refreshToken : refreshTokenList) {
            if(time < refreshToken.getExpirationTime()) break;
            refreshTokenRepository.delete(refreshToken);
            log.info("만료된 Refresh Token 삭제");
        }
    }
}
