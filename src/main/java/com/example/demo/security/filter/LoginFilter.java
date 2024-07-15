package com.example.demo.security.filter;

import com.example.demo.dto.member.MemberDetails;
import com.example.demo.security.JwtProvider;
import com.example.demo.security.refreshToken.RefreshToken;
import com.example.demo.security.refreshToken.RefreshTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        String username = obtainUsername(request);
        if(username == null) {
            log.error("아이디가 공백입니다");
            return null;
        }

        String password = obtainPassword(request);
        if(password == null) {
            log.error("비밀번호가 공백입니다");
            return null;
        }

        log.error("로그인 시도: {}", username);
        Authentication token = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(token);

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        log.error("로그인 성공");
        MemberDetails member = (MemberDetails) authResult.getPrincipal();

        String loginTime = String.valueOf(System.currentTimeMillis());

        String jwtToken = jwtProvider.createJwtToken(member);
        String refreshToken = jwtProvider.createRefreshToken(member, loginTime);

        RefreshToken saveToken = RefreshToken.builder()
                .refreshToken(refreshToken)
                .nickname(member.getNickname())
                .age(member.getAge())
                .role(member.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList().get(0))
                .createTime(loginTime)
                .count(jwtProvider.REFRESH_COUNT)
                .build();

        refreshTokenService.addRefreshToken(saveToken);

        response.addHeader(jwtProvider.JWT_HEADER_STRING, jwtProvider.TOKEN_PREFIX + jwtToken);
        response.addHeader(jwtProvider.REFRESH_HEADER_STRING, jwtProvider.TOKEN_PREFIX + refreshToken);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {

        log.error("로그인 실패");
        response.setStatus(401);

    }
}
