package com.example.demo.security.refreshtoken.refreshtokenservice.implement;

import com.example.demo.security.refreshtoken.domain.RefreshToken;
import com.example.demo.security.refreshtoken.domain.RefreshTokenRepository;
import com.example.demo.security.refreshtoken.refreshtokenservice.RefreshTokenService;
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

    @Override
    public void deleteById(String token) {
        refreshTokenRepository.deleteById(token);
    }

}
