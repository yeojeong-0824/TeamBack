package com.yeojeong.application.security.filter;

import com.yeojeong.application.config.exception.response.ExceptionResponseSender;
import com.yeojeong.application.domain.member.application.membernotification.MemberChangeService;
import com.yeojeong.application.domain.member.presentation.dto.MemberDetails;
import com.yeojeong.application.security.config.JwtProvider;
import com.yeojeong.application.security.config.refreshtoken.domain.RefreshToken;
import com.yeojeong.application.security.config.refreshtoken.application.RefreshTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final MemberChangeService memberChangeService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        String username = obtainUsername(request);
        if(username == null) {
            failLogin(request, response);
            return null;
        }

        String password = obtainPassword(request);
        if(password == null) {
            failLogin(request, response);
            return null;
        }

        log.info("로그인 시도: {}", username);
        Authentication token = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(token);

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        MemberDetails member = (MemberDetails) authResult.getPrincipal();
        memberChangeService.loginSuccessAndLastLoginDateChange(member.getMemberId());

        long loginTime = System.currentTimeMillis();

        String jwtToken = jwtProvider.createJwtToken(member);
        String refreshToken = jwtProvider.createRefreshToken(member, loginTime);

        RefreshToken saveToken = RefreshToken.builder()
                .id(refreshToken)
                .expirationTime(loginTime + jwtProvider.REFRESH_EXPIRATION_TIME)
                .ttl(jwtProvider.REFRESH_EXPIRATION_TIME / 1000)
                .build();

        refreshTokenService.save(saveToken);

        // Refresh Token - Cookie
        Cookie refreshCookie = new Cookie(jwtProvider.REFRESH_HEADER_STRING, jwtProvider.TOKEN_PREFIX_REFRESH + refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setMaxAge(jwtProvider.REFRESH_EXPIRATION_TIME / 1000);

        // JWT token
        response.addHeader(jwtProvider.JWT_HEADER_STRING, jwtProvider.TOKEN_PREFIX_JWT + jwtToken);
        response.addCookie(refreshCookie);

        response.setStatus(201);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        failLogin(request, response);
    }

    private void failLogin(HttpServletRequest request, HttpServletResponse response) {
        ExceptionResponseSender.createExceptionResponse(HttpStatus.UNAUTHORIZED.value(), request, response, "로그인에 실패했습니다.");
    }
}
