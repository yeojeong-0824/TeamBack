package com.yeojeong.application.security.config.refreshtoken.application;

import com.yeojeong.application.domain.member.presentation.dto.MemberDetails;
import com.yeojeong.application.security.config.refreshtoken.domain.RefreshToken;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface RefreshTokenService {
    void save(RefreshToken data);
    RefreshToken findById(String token);
    void delete(RefreshToken token);
    RefreshToken validRefresh(Cookie[] cookies, String token);
    String createRefresh(Long memberId, MemberDetails member);
}
