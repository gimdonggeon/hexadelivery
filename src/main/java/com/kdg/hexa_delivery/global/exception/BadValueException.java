package com.kdg.hexa_delivery.global.exception;

import lombok.Getter;

@Getter
public class BadValueException extends BusinessException {

    public BadValueException(final ExceptionType exceptionType) {
        super(exceptionType);
    }

}
