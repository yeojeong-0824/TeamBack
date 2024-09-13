package com.example.demo.security.filter;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.demo.domain.member.member.presentation.dto.MemberDetails;
import com.example.demo.domain.member.member.domain.Member;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        /*
        1. Jwt 재발급
        Step 1: Refresh 토큰을 DB에 있는 지 확인  > count 초과 시 null을 반환
        Step 2: Refresh 토큰이 null 이라면, 오류를 반환(401)
        Step 3: Refresh 토큰이 존재한다면 Jwt를 재발급
        Step 4: 필터 종료
        */
        String refreshTokenHeader = request.getHeader(jwtProvider.REFRESH_HEADER_STRING);
        if(refreshTokenHeader != null) {
            log.info("JWT Token 재발급");
            refreshTokenHeader = refreshTokenHeader.replace(jwtProvider.TOKEN_PREFIX, "");

            RefreshToken savedRefreshToken = refreshTokenService.findById(refreshTokenHeader);
            if(savedRefreshToken == null) {
                log.info("Refresh Token이 유효하지 않습니다");
                response.setStatus(401);
                return;
            }

            Member tokenMember = jwtProvider.decodeToken(refreshTokenHeader, String.valueOf(savedRefreshToken.getExpirationTime()));

            log.info("만료된 JWT Token 재발급 완료");
            MemberDetails reissueTokenMemberDetails = new MemberDetails(tokenMember);
            String jwtToken = jwtProvider.createJwtToken(reissueTokenMemberDetails);

            Cookie jwt = new Cookie(jwtProvider.JWT_HEADER_STRING, jwtToken);
            jwt.setMaxAge(jwtProvider.JWT_EXPIRATION_TIME / 1000);
            response.addCookie(jwt);

            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(reissueTokenMemberDetails, null, reissueTokenMemberDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
            return;
        }

        /*
        Jwt 확인
        Step 1: Jwt 토큰이 존재하는지 확인 -> 없다면 필터 종료
        Step 2: Jwt 토큰을 복호화 실패시(재발급 횟수 초과: 401, 변조: 400) 오류를 반환, 성공시 인증에 성공
        Step 3: 필터 종료
         */
        String jwtTokenHeader = request.getHeader(jwtProvider.JWT_HEADER_STRING);
        if(jwtTokenHeader == null) {
            filterChain.doFilter(request, response);
            return;
        }

        jwtTokenHeader = jwtTokenHeader.replace(jwtProvider.TOKEN_PREFIX, "");

        try {
            Member jwtTokenMember = jwtProvider.decodeToken(jwtTokenHeader, jwtProvider.SECRET);
            MemberDetails jwtTokenMemberDetails = new MemberDetails(jwtTokenMember);
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(jwtTokenMemberDetails, null, jwtTokenMemberDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch(TokenExpiredException e) {
            response.setStatus(401);
            log.info("JWT Token 유효기간이 만료되었습니다.");

        } catch (JWTDecodeException e) {
            log.info("JWT Token 값이 잘못되었습니다");

        } catch (JWTVerificationException e) {
            log.info("JWT Token 인증이 실패하였습니다");

        }

        filterChain.doFilter(request, response);
    }
}
