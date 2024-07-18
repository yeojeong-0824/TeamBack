package com.example.demo.config.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Set;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionMessage> handlerMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<String> errorMessage = fieldErrors.stream()
                .map(error -> error.getField() + ", " + error.getDefaultMessage())
                .toList();

        errorMessage.forEach(s -> log.error("유효성 검사에 실패하였습니다 (MethodArgumentNotValidException): " + s));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionMessage(errorMessage));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionMessage> handlerConstraintViolationException(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> set = ex.getConstraintViolations();
        List<String> errorMessage = set.stream()
                .map(error -> error.getPropertyPath() + ", " + error.getMessage())
                .toList();

        errorMessage.forEach(s -> log.error("유효성 검사에 실패하였습니다 (ConstraintViolationException): " + s));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionMessage(errorMessage));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionMessage> handlerDataIntegrityViolationException(DataIntegrityViolationException ex) {
        List<String> errorMessage = List.of("입력 값이 잘못되었습니다.");
        log.error("SQL 무결성 제약 조건이 위배되었습니다");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionMessage(errorMessage));
    }

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<ExceptionMessage> handlerServerException(ServerException ex) {
        List<String> errorMessage = List.of("일시적인 오류가 발생하였습니다.");
        log.warn(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionMessage(errorMessage));
    }
}
