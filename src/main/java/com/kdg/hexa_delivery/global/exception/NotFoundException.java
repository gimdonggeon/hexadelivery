package com.kdg.hexa_delivery.global.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends BusinessException {

    public NotFoundException(final ExceptionType exceptionType) {
        super(exceptionType);
    }

}
