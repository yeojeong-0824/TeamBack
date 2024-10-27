package com.yeojeong.application.security.filter;

import com.yeojeong.application.config.exception.response.ExceptionResponseSender;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ExceptionResponseSender.createExceptionResponse(HttpStatus.FORBIDDEN.value(), request, response, "권한이 없습니다.");
    }
}
