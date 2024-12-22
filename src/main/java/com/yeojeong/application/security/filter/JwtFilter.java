package com.yeojeong.application.security.filter;

import com.auth0.jwt.exceptions.*;
import com.yeojeong.application.config.exception.AuthedException;
import com.yeojeong.application.config.exception.response.ExceptionResponseSender;
import com.yeojeong.application.domain.member.domain.MemberDetails;
import com.yeojeong.application.domain.member.domain.Member;
import com.yeojeong.application.security.config.JwtProvider;
import com.yeojeong.application.security.config.refreshtoken.application.refreshtokenfacade.RefreshTokenFacade;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final RefreshTokenFacade refreshTokenFacade;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String jwtTokenHeader = request.getHeader(JwtProvider.JWT_HEADER_STRING);

        if(jwtTokenHeader == null) {
            filterChain.doFilter(request, response);
            return;
        }

        jwtTokenHeader = jwtTokenHeader.replace(JwtProvider.TOKEN_PREFIX_JWT, "");

        if(jwtTokenHeader.isEmpty() || jwtTokenHeader.equals("null")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Member jwtTokenMember = JwtProvider.decodeToken(jwtTokenHeader, JwtProvider.SECRET);
            MemberDetails jwtTokenMemberDetails = new MemberDetails(jwtTokenMember);
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(jwtTokenMemberDetails, null, jwtTokenMemberDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (TokenExpiredException ex) {
            try {
                Cookie[] cookies = request.getCookies();

                MemberDetails memberDetails = refreshTokenFacade.getMemberDetailsByRefreshToken(cookies);
                Cookie refreshCookie = refreshTokenFacade.createNewRefreshTokenCookie(memberDetails);
                response.addCookie(refreshCookie);

                String jwtToken = refreshTokenFacade.createNewJwtToken(memberDetails);
                response.addHeader(JwtProvider.JWT_HEADER_STRING, JwtProvider.TOKEN_PREFIX_JWT + jwtToken);
            } catch(AuthedException e) {
                ExceptionResponseSender.createExceptionResponse(HttpStatus.FORBIDDEN.value(), request, response, e.getMessage());
                return;
            } catch (RuntimeException e) {
                ExceptionResponseSender.createExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), request, response, ex.getMessage());
                return;
            }
        } catch (JWTDecodeException ex) {
            ExceptionResponseSender.createExceptionResponse(HttpStatus.BAD_REQUEST.value(), request, response, "잘못된 토큰입니다.");
            return;
        } catch (SignatureVerificationException ex) {
            ExceptionResponseSender.createExceptionResponse(HttpStatus.FORBIDDEN.value(), request, response, "서명 검증에 실패했습니다.");
            return;
        } catch (AlgorithmMismatchException ex) {
            ExceptionResponseSender.createExceptionResponse(HttpStatus.FORBIDDEN.value(), request, response, "알고리즘이 일치하지 않습니다.");
            return;
        } catch (Exception ex) {
            ExceptionResponseSender.createExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), request, response, ex.getMessage());
            return;
        }


        filterChain.doFilter(request, response);
    }

}
