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
        log.error(errorCode.getHttpStatus() + " 잘못된 요청입니다.");
        return ResponseEntity
                .status(errorCode.getHttpStatus().value())
                .body(new ErrorResponse(errorCode));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleException(Exception e){
        log.error("GlobalExceptionHandler 작동");

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

    // origin
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionMessage> handlerDataIntegrityViolationException(DataIntegrityViolationException ex) {
        List<String> errorMessage = List.of("입력 값이 잘못되었습니다.");
        log.error("SQL 무결성 제약 조건이 위배되었습니다");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionMessage(errorMessage));
    }

    /*@ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionMessage> handlerDataIntegrityViolationException(DataIntegrityViolationException ex) {
        ErrorCode errorCode = ex.
        log.error("SQL 무결성 제약 조건이 위배되었습니다");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionMessage(errorMessage));
    }*/

    /*@ExceptionHandler(ServerException.class)
    public ResponseEntity<ExceptionMessage> handlerServerException(ServerException ex) {
        List<String> errorMessage = List.of("일시적인 오류가 발생하였습니다.");
        log.warn(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionMessage(errorMessage));
    }

    @ExceptionHandler(RequestDataException.class)
    public ResponseEntity<ExceptionMessage> handlerRequestDataException(RequestDataException ex) {
        List<String> errorMessage = List.of(ex.getMessage());
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionMessage(errorMessage));
    }*/

    @ExceptionHandler(NotFoundDataException.class)
    public ResponseEntity<ExceptionMessage> handlerNotFoundMemberException(NotFoundDataException ex) {
        List<String> errorMessage = List.of(ex.getMessage());
        errorMessage.forEach(log::error);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionMessage(errorMessage));
    }

    @ExceptionHandler(DuplicatedException.class)
    public ResponseEntity<ExceptionMessage> handlerDuplicatedException(DuplicatedException ex) {
        List<String> errorMessage = List.of(ex.getMessage());
        errorMessage.forEach(log::error);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionMessage(errorMessage));
    }

   /* @ExceptionHandler(AuthorityException.class)
    public ResponseEntity<ExceptionMessage> handlerAuthorityException(AuthorityException ex) {
        List<String> errorMessage = List.of(ex.getMessage());
        errorMessage.forEach(log::error);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionMessage(errorMessage));
    }*/
}
