package com.example.demo.config.exception.member;

import com.example.demo.config.exception.ExceptionMessage;
import com.example.demo.config.exception.RequestDataException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class MemberExceptionHandler {
    @ExceptionHandler(NotFoundMemberException.class)
    public ResponseEntity<ExceptionMessage> handlerNotFoundMemberException(NotFoundMemberException ex) {
        List<String> errorMessage = List.of(ex.getMessage());
        errorMessage.forEach(log::error);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionMessage(errorMessage));
    }
}
