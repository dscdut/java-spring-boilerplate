package com.gdsc.boilerplate.springboot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomControllerAdvice {

	@ExceptionHandler(CustomException.class)
	ResponseEntity<ApiExceptionResponse> handleCustomException(CustomException exception) {

		final ApiExceptionResponse response = new ApiExceptionResponse(exception.getErrorMessage(), 
				exception.getCode());

		return ResponseEntity.status(toHttpStatus(exception.getCode())).body(response);
	}
	
	private HttpStatus toHttpStatus(int code) {
		switch (code) {
		case 101:
			return HttpStatus.BAD_REQUEST;
		case 102:
			return HttpStatus.UNAUTHORIZED;
		case 103:
			return HttpStatus.INTERNAL_SERVER_ERROR;
		case 104:
			return HttpStatus.CONFLICT;

		}
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}

}
