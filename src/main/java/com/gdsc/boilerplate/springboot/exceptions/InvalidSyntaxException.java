package com.gdsc.boilerplate.springboot.exceptions;

import com.gdsc.boilerplate.springboot.utils.ExceptionMessageAccessor;

public class InvalidSyntaxException extends CustomException {

	private static final long serialVersionUID = 7721177071697621360L;

	public InvalidSyntaxException(ExceptionMessageAccessor exceptionMessageAccessor) {
        super(ExceptionConstants.BAD_REQUEST.getCode(),
                exceptionMessageAccessor.getMessage(null, ExceptionConstants.BAD_REQUEST.getMessageName()));
    }
}
