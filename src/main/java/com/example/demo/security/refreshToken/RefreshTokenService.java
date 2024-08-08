package com.example.demo.security.refreshToken;

public interface RefreshTokenService {
    void save(RefreshToken data);
    RefreshToken findById(String token);
    void deleteById(String token);
}
