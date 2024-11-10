package com.yeojeong.application.security.config.refreshtoken.application.implement;

import com.yeojeong.application.config.exception.AuthedException;
import com.yeojeong.application.domain.member.presentation.dto.MemberDetails;
import com.yeojeong.application.security.config.JwtProvider;
import com.yeojeong.application.security.config.refreshtoken.domain.RefreshToken;
import com.yeojeong.application.security.config.refreshtoken.domain.RefreshTokenRepository;
import com.yeojeong.application.security.config.refreshtoken.application.RefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;

    @Override
    public void save(RefreshToken data) {
        refreshTokenRepository.save(data);
    }

    @Override
    public RefreshToken findById(String token) {
        Optional<RefreshToken> savedOptionalRefreshToken = refreshTokenRepository.findById(token);
        if(savedOptionalRefreshToken.isEmpty()) throw new AuthedException("Refresh Token 내용이 존재하지 않습니다.");

        RefreshToken savedRefreshToken = savedOptionalRefreshToken.get();

        return savedRefreshToken;
    }

    @Override
    public void delete(RefreshToken token) {
        refreshTokenRepository.delete(token);
    }

    @Override
    public RefreshToken validRefresh (Cookie[] cookies, String refreshTokenHeader) {
        try {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(jwtProvider.REFRESH_HEADER_STRING)) refreshTokenHeader = cookie.getValue();
            }
        } catch (Exception e) {
            throw new AuthedException("Refresh Token 쿠키에 존재하지 않습니다.");
        }

        if (refreshTokenHeader == null) throw new AuthedException("Refresh Token 쿠키에 존재하지 않습니다.");

        refreshTokenHeader = refreshTokenHeader.replace(jwtProvider.TOKEN_PREFIX_REFRESH, "");

        RefreshToken savedRefreshToken = findById(refreshTokenHeader);

        return savedRefreshToken;
    }

    @Override
    public String createRefresh(Long memberId, MemberDetails member){

        long loginTime = System.currentTimeMillis();
        String refreshToken = jwtProvider.createRefreshToken(member, loginTime);

        RefreshToken saveToken = RefreshToken.builder()
                .id(refreshToken)
                .expirationTime(loginTime + jwtProvider.REFRESH_EXPIRATION_TIME)
                .ttl(jwtProvider.REFRESH_EXPIRATION_TIME / 1000)
                .build();

        refreshTokenRepository.save(saveToken);

        return refreshToken;
    }

    @Override
    public Cookie setRefreshCookie (String refreshToken) {
        Cookie refreshCookie = new Cookie(jwtProvider.REFRESH_HEADER_STRING, jwtProvider.TOKEN_PREFIX_REFRESH + refreshToken);
        refreshCookie.setAttribute("SameSite", "None");
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");

        return refreshCookie;
    }
}
