package com.example.demo.security.filter;

import com.example.demo.domain.member.member.application.membernotification.MemberChangeService;
import com.example.demo.domain.member.member.presentation.dto.MemberDetails;
import com.example.demo.security.JwtProvider;
import com.example.demo.security.refreshtoken.domain.RefreshToken;
import com.example.demo.security.refreshtoken.refreshtokenservice.RefreshTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
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
            log.error("아이디가 공백입니다");
            response.setStatus(401);
            return null;
        }

        String password = obtainPassword(request);
        if(password == null) {
            log.error("비밀번호가 공백입니다");
            response.setStatus(401);
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

        /*
        로그인에 성공했다면 Jwt Token과 Refresh Token을 발급

        JWT Token: 사용자 정보를 Secret Key를 이용하여 암호화 후 Header에 넣어줌
        Refresh Token: 사용자 정보를 로그인이 성공한 시점으로 암호화 후 DB에 저장 후 Header에 넣어줌
         */

        log.info("로그인 성공");
        MemberDetails member = (MemberDetails) authResult.getPrincipal();

        // 로그인 성공으로 마지막 로그인 시간 변경
        memberChangeService.loginSuccessAndLastLoginDateChange(member.getMemberId());

        long loginTime = System.currentTimeMillis();

        String jwtToken = jwtProvider.createJwtToken(member);
        String refreshToken = jwtProvider.createRefreshToken(member, loginTime);

        RefreshToken saveToken = RefreshToken.builder()
                .id(refreshToken)
                .expirationTime(loginTime + jwtProvider.REFRESH_EXPIRATION_TIME)
                .count(jwtProvider.REFRESH_COUNT)
                .ttl(jwtProvider.REFRESH_EXPIRATION_TIME / 1000)
                .build();

        refreshTokenService.save(saveToken);

        // jwt Token
        Cookie jwt = new Cookie(jwtProvider.JWT_HEADER_STRING, jwtToken);
        jwt.setMaxAge(jwtProvider.JWT_EXPIRATION_TIME / 1000);

        response.addCookie(jwt);
        response.setStatus(201);

        // refresh Token
        Cookie refresh = new Cookie(jwtProvider.REFRESH_HEADER_STRING, refreshToken);
        refresh.setMaxAge(jwtProvider.REFRESH_EXPIRATION_TIME / 1000);

        response.addCookie(refresh);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {

        log.error("로그인 실패");
        response.setStatus(401);
    }
}
