package com.kdg.hexa_delivery.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    // @valid 유효성 검사 예외처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

        // 유효성 검사 오류 메시지를 필드별로 수집
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        // 에러 응답 생성
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), errors);

        log.error("[MethodArgumentNotValidException] : {}", exceptionResponse.getErrors());

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ExceptionResponse> handleMethodIllegalState(IllegalStateException ex) {

        // 유효성 검사 오류 메시지를 필드별로 수집
        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());

        // 에러 응답 생성
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), errors);

        log.error("IllegalStateException] : {}", exceptionResponse.getErrors());

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    // 비지니스 로직 예외처리
    @ExceptionHandler({BusinessException.class, NotFoundException.class,
            BadValueException.class, BadRequestException.class, WrongAccessException.class})
    public ResponseEntity<ExceptionResponse> handleBusinessException(BusinessException ex) {
        ExceptionType exceptionType = ex.getExceptionType();

        Map<String, String> errors = new HashMap<>();
        errors.put(exceptionType.name(), exceptionType.getMessage());

        // 에러 응답 생성
        ExceptionResponse exceptionResponse = new ExceptionResponse(exceptionType.getStatus(), exceptionType.getStatus().value(), errors);

        log.error("[ {} ] - {} : {}", ex.getClass(), exceptionType.getStatus(), exceptionResponse.getErrors());

        return new ResponseEntity<>(exceptionResponse, exceptionType.getStatus());
    }

}
