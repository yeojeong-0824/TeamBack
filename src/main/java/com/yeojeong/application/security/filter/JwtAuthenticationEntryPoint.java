package com.yeojeong.application.security.filter;

import com.yeojeong.application.config.exception.ErrorResponse;
import com.yeojeong.application.config.exception.handler.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeojeong.application.security.FilterExceptionHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final FilterExceptionHandler filterExceptionHandler;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("AuthenticationEntryPoint 작동");

        ErrorCode errorCode = (ErrorCode) request.getAttribute("exception");

        if (errorCode == null) {
            errorCode = ErrorCode.UNAUTHORIZED_CLIENT;
        }

        filterExceptionHandler.filterException(errorCode, response);

    }
}
