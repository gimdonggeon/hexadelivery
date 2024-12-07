package com.kdg.hexa_delivery.global.exception;

import lombok.Getter;

@Getter
public class WrongAccessException extends BusinessException {

     public WrongAccessException(final ExceptionType exceptionType) {
         super(exceptionType);
    }

}
