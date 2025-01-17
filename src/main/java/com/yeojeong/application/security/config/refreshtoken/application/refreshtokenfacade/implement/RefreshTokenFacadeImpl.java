package com.yeojeong.application.security.config.refreshtoken.application.refreshtokenfacade.implement;

import com.yeojeong.application.config.exception.AuthedException;
import com.yeojeong.application.domain.member.domain.Member;
import com.yeojeong.application.domain.member.domain.MemberDetails;
import com.yeojeong.application.security.config.JwtProvider;
import com.yeojeong.application.security.config.refreshtoken.application.refreshtokenfacade.RefreshTokenFacade;
import com.yeojeong.application.security.config.refreshtoken.application.refreshtokenservice.RefreshTokenService;
import com.yeojeong.application.security.config.refreshtoken.domain.RefreshToken;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenFacadeImpl implements RefreshTokenFacade {

    private final RefreshTokenService refreshTokenService;

    @Override
    @Transactional
    public void remoteRefreshToken(Cookie[] cookies) {
        String refreshToken = getRefreshTokenByCookie(cookies);
        RefreshToken refreshTokenEntity = refreshTokenService.findById(refreshToken);
        refreshTokenService.delete(refreshTokenEntity);
    }

    @Override
    @Transactional
    public MemberDetails getMemberDetailsByRefreshToken(Cookie[] cookies) {
        String refreshToken = getRefreshTokenByCookie(cookies);
        RefreshToken refreshTokenEntity = refreshTokenService.findById(refreshToken);
        refreshTokenService.delete(refreshTokenEntity);

        Member member = JwtProvider.decodeToken(refreshToken, refreshTokenEntity.getKey());
        return new MemberDetails(member);
    }

    @Override
    public Cookie createNewRefreshTokenCookie(MemberDetails memberDetails) {
        String loginTime = Long.toString(System.currentTimeMillis());

        String refreshToken = JwtProvider.createRefreshToken(memberDetails, loginTime);
        RefreshToken saveToken = RefreshToken.builder()
                .id(refreshToken)
                .key(loginTime)
                .ttl(JwtProvider.REFRESH_EXPIRATION_TIME / 1000)
                .build();

        refreshTokenService.save(saveToken);

        Cookie cookie = new Cookie(JwtProvider.REFRESH_HEADER_STRING, refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(JwtProvider.REFRESH_EXPIRATION_TIME / 1000);

        return cookie;
    }

    @Override
    public String createNewJwtToken(MemberDetails memberDetails) {
        return JwtProvider.createJwtToken(memberDetails);
    }

    private String getRefreshTokenByCookie(Cookie[] cookies) {
        if(cookies == null) throw new AuthedException("Refresh token이 존재하지 않습니다.");
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(JwtProvider.REFRESH_HEADER_STRING))
                return cookie.getValue();
        }
        throw new AuthedException("Refresh token이 존재하지 않습니다.");
    }
}
