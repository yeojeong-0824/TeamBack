package com.yeojeong.application.security.filter;

import com.auth0.jwt.exceptions.*;
import com.yeojeong.application.config.exception.response.ExceptionResponseSender;
import com.yeojeong.application.domain.member.presentation.dto.MemberDetails;
import com.yeojeong.application.domain.member.domain.Member;
import com.yeojeong.application.security.config.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

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

        try {
            Member jwtTokenMember = JwtProvider.decodeToken(jwtTokenHeader, JwtProvider.SECRET);
            MemberDetails jwtTokenMemberDetails = new MemberDetails(jwtTokenMember);
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(jwtTokenMemberDetails, null, jwtTokenMemberDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JWTDecodeException ex) {
            ExceptionResponseSender.createExceptionResponse(HttpStatus.UNAUTHORIZED.value(), request, response, "잘못된 토큰입니다.");
            return;
        } catch (SignatureVerificationException ex) {
            ExceptionResponseSender.createExceptionResponse(HttpStatus.UNAUTHORIZED.value(), request, response, "서명 검증에 실패했습니다.");
            return;
        } catch (AlgorithmMismatchException ex) {
            ExceptionResponseSender.createExceptionResponse(HttpStatus.UNAUTHORIZED.value(), request, response, "알고리즘이 일치하지 않습니다.");
            return;
        } catch (Exception ex) {
            ExceptionResponseSender.createExceptionResponse(HttpStatus.UNAUTHORIZED.value(), request, response, ex.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }

}
