package com.example.demo.security.refreshtoken.refreshtokenservice;

import com.example.demo.security.refreshtoken.domain.RefreshToken;

public interface RefreshTokenService {
    void save(RefreshToken data);
    RefreshToken findById(String token);
}
