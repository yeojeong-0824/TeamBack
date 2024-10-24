package com.yeojeong.application.security.filter;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.yeojeong.application.config.exception.handler.ErrorCode;
import com.yeojeong.application.domain.member.member.presentation.dto.MemberDetails;
import com.yeojeong.application.domain.member.member.domain.Member;
import com.yeojeong.application.security.config.JwtProvider;
import com.yeojeong.application.security.config.refreshtoken.application.RefreshTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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

        jwtTokenHeader = jwtTokenHeader.replace(jwtProvider.TOKEN_PREFIX_JWT, "");

        try {
            Member jwtTokenMember = jwtProvider.decodeToken(jwtTokenHeader, jwtProvider.SECRET);
            MemberDetails jwtTokenMemberDetails = new MemberDetails(jwtTokenMember);
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(jwtTokenMemberDetails, null, jwtTokenMemberDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            ErrorCode errorCode = null;
            response.setStatus(401);

            if (e instanceof TokenExpiredException) {
                errorCode =  ErrorCode.EXPIRED_TOKEN;

            } else if (e instanceof JWTDecodeException) {
                errorCode = ErrorCode.JWT_DECODE_FAIL;

            } else if (e instanceof JWTVerificationException) {
                errorCode = ErrorCode.JWT_SIGNATURE_FAIL;
            }

            request.setAttribute("exception", errorCode);
        }

        filterChain.doFilter(request, response);
    }

}
