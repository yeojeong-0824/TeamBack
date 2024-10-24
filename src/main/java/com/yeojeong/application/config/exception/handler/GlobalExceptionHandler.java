package com.yeojeong.application.config.exception.handler;

import com.yeojeong.application.config.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(RestApiException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        log.error(errorCode.getMessage());
        return ResponseEntity
                .status(errorCode.getHttpStatus().value())
                .body(new ErrorResponse(errorCode));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleException(Exception e){
        log.error("GlobalExceptionHandler 작동");
        log.error(e.getClass().getName());

        ErrorCode errorCode = ErrorCode.BAD_REQUEST;
        return ResponseEntity
                .status(errorCode.getHttpStatus().value())
                .body(new ErrorResponse(errorCode));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionMessage> handlerMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<String> errorMessage = fieldErrors.stream()
                .map(error -> error.getField() + ", " + error.getDefaultMessage())
                .toList();

        errorMessage.forEach(s -> log.error("유효성 검사에 실패하였습니다: " + s));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionMessage(errorMessage));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handlerDataIntegrityViolationException(DataIntegrityViolationException ex) {
        ErrorCode errorCode = ErrorCode.SQL_INTEGRITY_VIOLATION;
        log.error("SQL 무결성 제약 조건이 위배되었습니다");
        return ResponseEntity.status(errorCode.getHttpStatus()).body(new ErrorResponse(errorCode));
    }

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<ErrorResponse> handlerServerException(ServerException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        log.error(errorCode.getMessage());
        return ResponseEntity.status(errorCode.getHttpStatus()).body(new ErrorResponse(errorCode));
    }

    @ExceptionHandler(RequestDataException.class)
    public ResponseEntity<ErrorResponse> handlerRequestDataException(RequestDataException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        log.error(errorCode.getMessage());
        return ResponseEntity.status(errorCode.getHttpStatus()).body(new ErrorResponse(errorCode));
    }

    @ExceptionHandler(NotFoundDataException.class)
    public ResponseEntity<ErrorResponse> handlerNotFoundMemberException(NotFoundDataException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        log.error(errorCode.getMessage());
        return ResponseEntity.status(errorCode.getHttpStatus()).body(new ErrorResponse(errorCode));
    }

    @ExceptionHandler(DuplicatedException.class)
    public ResponseEntity<ErrorResponse> handlerDuplicatedException(DuplicatedException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        log.error(errorCode.getMessage());
        return ResponseEntity.status(errorCode.getHttpStatus()).body(new ErrorResponse(errorCode));
    }
}
