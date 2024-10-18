package com.yeojeong.application.security.filter.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeojeong.application.config.exception.ErrorResponse;
import com.yeojeong.application.config.exception.handler.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service

// JWT 오류 수행 Handler
public class JwtException {
    public void filterException(ErrorCode errorCode, HttpServletResponse response) throws IOException {

        log.error(errorCode.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(errorCode);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonErrorResponse = objectMapper.writeValueAsString(errorResponse);

        response.setStatus(errorCode.getHttpStatus().value());
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); // application/json
        response.getWriter().write(jsonErrorResponse);

    }
}
