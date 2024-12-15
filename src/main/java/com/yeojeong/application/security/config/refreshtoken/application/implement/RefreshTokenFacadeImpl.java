package com.yeojeong.application.security.config.refreshtoken.application.implement;

import com.yeojeong.application.config.exception.AuthedException;
import com.yeojeong.application.domain.member.domain.Member;
import com.yeojeong.application.domain.member.presentation.dto.MemberDetails;
import com.yeojeong.application.security.config.JwtProvider;
import com.yeojeong.application.security.config.refreshtoken.application.RefreshTokenFacade;
import com.yeojeong.application.security.config.refreshtoken.application.RefreshTokenService;
import com.yeojeong.application.security.config.refreshtoken.domain.RefreshToken;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenFacadeImpl implements RefreshTokenFacade {

    private final RefreshTokenService refreshTokenService;
    private final JwtProvider jwtProvider;

    public MemberDetails getMemberDetailsByRefreshToken(Cookie[] cookies) {
        String refreshToken = RefreshTokenFacadeImpl.getRefreshTokenByCookie(cookies);
        RefreshToken refreshTokenEntity = refreshTokenService.findById(refreshToken);
        refreshTokenService.delete(refreshTokenEntity);

        Member member = jwtProvider.decodeToken(refreshToken, refreshTokenEntity.getKey());
        return new MemberDetails(member);
    }

    public Cookie createNewRefreshTokenCookie(MemberDetails memberDetails) {
        String newRefreshToken = RefreshTokenFacadeImpl.createRefreshToken(refreshTokenService, memberDetails);
        return RefreshTokenFacadeImpl.createRefreshCookie(newRefreshToken);
    }

    public String createNewJwtToken(MemberDetails memberDetails) {
        return jwtProvider.createJwtToken(memberDetails);
    }

    static public String createRefreshToken(RefreshTokenService refreshTokenService, MemberDetails member) {
        String loginTime = Long.toString(System.currentTimeMillis());

        String refreshToken = JwtProvider.createRefreshToken(member, loginTime);
        RefreshToken saveToken = RefreshToken.builder()
                .id(refreshToken)
                .key(loginTime)
                .ttl(JwtProvider.REFRESH_EXPIRATION_TIME / 1000)
                .build();

        refreshTokenService.save(saveToken);
        return refreshToken;
    }

    static public Cookie createRefreshCookie(String refreshToken) {
        Cookie refreshCookie = new Cookie(JwtProvider.REFRESH_HEADER_STRING, refreshToken);
        refreshCookie.setAttribute("SameSite", "None");
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");

        return refreshCookie;
    }

    static public String getRefreshTokenByCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(JwtProvider.REFRESH_HEADER_STRING))
                return cookie.getValue();
        }
        throw new AuthedException("Refresh token이 존재하지 않습니다.");
    }
}
