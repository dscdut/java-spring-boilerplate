package com.gdsc.boilerplate.springboot.exceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gdsc.boilerplate.springboot.utils.ExceptionMessageAccessor;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {
	@Autowired
	private ExceptionMessageAccessor accessor;

    @ExceptionHandler(InternalServerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handleConversion(RuntimeException ex) {
    	final ApiExceptionResponse response = new ApiExceptionResponse(ExceptionConstants.INTERNAL_SERVER_ERROR.getCode(),
				accessor.getMessage(null, ExceptionConstants.INTERNAL_SERVER_ERROR.getMessageName()));
    	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	ResponseEntity<ValidationExceptionResponse> handleMethodArgumentNotValidException(
			MethodArgumentNotValidException exception) {

		final List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
		final List<String> errorList = fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
				.collect(Collectors.toList());

		final ValidationExceptionResponse response = new ValidationExceptionResponse(
				errorList, ExceptionConstants.INVALID_INPUT_REGISTRATION.getCode(),
				accessor.getMessage(null, ExceptionConstants.INVALID_INPUT_REGISTRATION.getMessageName()));

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

}
