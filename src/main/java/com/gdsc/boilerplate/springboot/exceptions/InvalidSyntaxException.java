package com.gdsc.boilerplate.springboot.exceptions;

import com.gdsc.boilerplate.springboot.utils.ExceptionMessageAccessor;

public class InvalidSyntaxException extends CustomException {
    public InvalidSyntaxException(ExceptionMessageAccessor exceptionMessageAccessor) {
        super(ExceptionConstants.BAD_REQUEST.getCode(),
                exceptionMessageAccessor.getMessage(null, ExceptionConstants.BAD_REQUEST.getMessageName(), null));
    }
}
