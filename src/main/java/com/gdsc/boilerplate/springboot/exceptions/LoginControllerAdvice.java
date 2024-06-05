package com.gdsc.boilerplate.springboot.exceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gdsc.boilerplate.springboot.controller.LoginController;
import com.gdsc.boilerplate.springboot.utils.ExceptionMessageAccessor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice(basePackageClasses = LoginController.class)
public class LoginControllerAdvice {
	@Autowired
	private ExceptionMessageAccessor accessor;
	
	@ExceptionHandler(InvalidAuthenticationException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	ResponseEntity<ApiExceptionResponse> handleInvalidAuthenticationException(InvalidAuthenticationException exception) {

		final ApiExceptionResponse response = new ApiExceptionResponse(ExceptionConstants.INVALID_AUTHENTICATED.getCode(),
				accessor.getMessage(null, ExceptionConstants.INVALID_AUTHENTICATED.getMessageName()));
		log.warn("InvalidAuthenticationException: {}", response.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}
	
	@ExceptionHandler(InvalidSyntaxLoginException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	ResponseEntity<ApiExceptionResponse> handleInvalidSyntaxLoginException(InvalidSyntaxLoginException exception) {

		final ApiExceptionResponse response = new ApiExceptionResponse(ExceptionConstants.INVALID_INPUT_LOGIN.getCode(),
				accessor.getMessage(null, ExceptionConstants.INVALID_INPUT_LOGIN.getMessageName()));
		log.warn("InvalidSyntaxLoginException: {}", response.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
}
