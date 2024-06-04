package com.gdsc.boilerplate.springboot.exceptions;

import com.gdsc.boilerplate.springboot.utils.ExceptionMessageAccessor;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class InternalServerException extends CustomException {
    public InternalServerException(ExceptionMessageAccessor exceptionMessageAccessor) {
        super(ExceptionConstants.INTERNAL_SERVER_ERROR.getCode(),
                exceptionMessageAccessor.getMessage(null, ExceptionConstants.INTERNAL_SERVER_ERROR.getMessageName(), null));
    }
}
