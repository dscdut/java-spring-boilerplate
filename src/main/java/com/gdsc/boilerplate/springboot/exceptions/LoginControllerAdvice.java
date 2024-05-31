package com.gdsc.boilerplate.springboot.exceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gdsc.boilerplate.springboot.controller.LoginController;
import com.gdsc.boilerplate.springboot.utils.ExceptionMessageAccessor;

@RestControllerAdvice(basePackageClasses = LoginController.class)
public class LoginControllerAdvice {

	@Autowired
	private ExceptionMessageAccessor exceptionMessageAccessor;

	@ExceptionHandler(BadCredentialsException.class)
	ResponseEntity<ApiExceptionResponse> handleRegistrationException(BadCredentialsException exception) {

		final ApiExceptionResponse response = new ApiExceptionResponse(
				exceptionMessageAccessor.getMessage(null, ExceptionConstants.INVALID_AUTHENTICATED.getMessageName(), null),
				ExceptionConstants.INVALID_AUTHENTICATED.getCode());

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}

}
