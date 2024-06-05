package com.gdsc.boilerplate.springboot.exceptions;

import com.gdsc.boilerplate.springboot.controller.LoginController;
import com.gdsc.boilerplate.springboot.utils.ExceptionMessageAccessor;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = LoginController.class)
public class InvalidAuthenticationException extends CustomException {

	private static final long serialVersionUID = 3348247949778412606L;

	public InvalidAuthenticationException(ExceptionMessageAccessor exceptionMessageAccessor) {
        super(ExceptionConstants.INVALID_AUTHENTICATED.getCode(),
                exceptionMessageAccessor.getMessage(null, ExceptionConstants.INVALID_AUTHENTICATED.getMessageName()));
    }
}
