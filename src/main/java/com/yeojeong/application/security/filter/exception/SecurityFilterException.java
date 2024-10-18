package com.yeojeong.application.security.filter.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeojeong.application.config.exception.ErrorResponse;
import com.yeojeong.application.config.exception.handler.ErrorCode;
import com.yeojeong.application.security.filter.exception.FilterException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
// LoginFilter 오류 수쟁 핸들러
public class SecurityFilterException extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (FilterException exception) {
            setErrorResponse(exception.errorCode, response, exception);
        } catch (RuntimeException exception) {
            setErrorResponse(ErrorCode.BAD_REQUEST, response, exception);
        }
    }

    public void setErrorResponse(ErrorCode errorCode, HttpServletResponse response, Throwable ex) throws IOException {

        ErrorResponse errorResponse = new ErrorResponse(errorCode);

        log.error(errorCode.getMessage());

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonErrorResponse = objectMapper.writeValueAsString(errorResponse);

        response.setStatus(errorCode.getHttpStatus().value());
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); // application/json

        response.getWriter().write(jsonErrorResponse);
    }
}
