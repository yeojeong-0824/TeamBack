package com.yeojeong.application.config.exception.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeojeong.application.config.exception.handler.GlobalExceptionHandler;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@Slf4j
public class ExceptionResponseSender {
    private ExceptionResponseSender() {}

    public static void createExceptionResponse(int httpStatus, HttpServletRequest request, HttpServletResponse response, String message) {
        final String path = request.getRequestURI();
        log.error("Path: {}, Status: {}, Exception: {}", path, httpStatus, message);

        final ExceptionResponse errorInfo = new ExceptionResponse(httpStatus, path, message);
        final ObjectMapper mapper = new ObjectMapper();

        try {
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write(mapper.writeValueAsString(errorInfo));
            response.setStatus(httpStatus);
        } catch (IOException e) {
            log.error("응답 작성 실패: {}", e.getMessage(), e);
        }
    }
}
