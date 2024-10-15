package com.yeojeong.application.security.refreshtoken.refreshtokenservice;

import com.yeojeong.application.security.refreshtoken.domain.RefreshToken;

public interface RefreshTokenService {
    void save(RefreshToken data);
    RefreshToken findById(String token);
}
