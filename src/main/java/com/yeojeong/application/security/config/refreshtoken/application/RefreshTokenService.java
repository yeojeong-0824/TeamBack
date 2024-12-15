package com.yeojeong.application.security.config.refreshtoken.application;

import com.yeojeong.application.security.config.refreshtoken.domain.RefreshToken;

public interface RefreshTokenService {
    void save(RefreshToken data);
    RefreshToken findById(String token);
    void delete(RefreshToken token);
}
