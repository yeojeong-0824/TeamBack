package com.example.demo.security.filter;

import com.example.demo.config.exception.ErrorResponse;
import com.example.demo.config.exception.handler.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.UNAUTHORIZED_CLIENT);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonErrorResponse = objectMapper.writeValueAsString(errorResponse);

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(jsonErrorResponse);
    }
}
