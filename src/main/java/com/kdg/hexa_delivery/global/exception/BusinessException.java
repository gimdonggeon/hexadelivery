package com.kdg.hexa_delivery.global.exception;

import lombok.Getter;

@Getter
public abstract class BusinessException extends RuntimeException {
    private final ExceptionType exceptionType;

    public BusinessException(final ExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }
}
