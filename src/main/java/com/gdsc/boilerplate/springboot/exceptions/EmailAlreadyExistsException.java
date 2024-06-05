package com.gdsc.boilerplate.springboot.exceptions;

import com.gdsc.boilerplate.springboot.controller.RegistrationController;
import com.gdsc.boilerplate.springboot.utils.ExceptionMessageAccessor;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = RegistrationController.class)
public class EmailAlreadyExistsException extends CustomException {

	private static final long serialVersionUID = -6980342143902904694L;

	public EmailAlreadyExistsException(ExceptionMessageAccessor exceptionMessageAccessor) {
        super(ExceptionConstants.EMAIL_ALREADY_EXISTS.getCode(),
                exceptionMessageAccessor.getMessage(null, ExceptionConstants.EMAIL_ALREADY_EXISTS.getMessageName()));
    }
}