package com.yeojeong.application.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeojeong.application.config.exception.ErrorResponse;
import com.yeojeong.application.config.exception.handler.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class FilterExceptionHandler {
    public void filterException(ErrorCode errorCode, HttpServletResponse response) throws IOException {

        ErrorResponse errorResponse = new ErrorResponse(errorCode);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonErrorResponse = objectMapper.writeValueAsString(errorResponse);

        response.setStatus(errorCode.getHttpStatus().value());
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); // application/json
        response.getWriter().write(jsonErrorResponse);

    }
}
