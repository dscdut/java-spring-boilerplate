package com.gdsc.boilerplate.springboot.exceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gdsc.boilerplate.springboot.controller.RegistrationController;
import com.gdsc.boilerplate.springboot.utils.ExceptionMessageAccessor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice(basePackageClasses = RegistrationController.class)
public class RegistrationControllerAdvice {
	@Autowired
	private ExceptionMessageAccessor accessor;

	@ExceptionHandler(EmailAlreadyExistsException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	ResponseEntity<ApiExceptionResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException exception) {

		final ApiExceptionResponse response = new ApiExceptionResponse(
				ExceptionConstants.EMAIL_ALREADY_EXISTS.getCode(),
				accessor.getMessage(null, ExceptionConstants.EMAIL_ALREADY_EXISTS.getMessageName()));
		log.warn("EmailAlreadyExistsException: {}", response.getMessage());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}

	@ExceptionHandler(InvalidSyntaxRegistrationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	ResponseEntity<ApiExceptionResponse> handleInvalidSyntaxRegistrationException(
			InvalidSyntaxRegistrationException exception) {

		final ApiExceptionResponse response = new ApiExceptionResponse(
				ExceptionConstants.INVALID_INPUT_REGISTRATION.getCode(),
				accessor.getMessage(null, ExceptionConstants.INVALID_INPUT_REGISTRATION.getMessageName()));
		log.warn("InvalidSyntaxRegistrationException: {}", response.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
}
