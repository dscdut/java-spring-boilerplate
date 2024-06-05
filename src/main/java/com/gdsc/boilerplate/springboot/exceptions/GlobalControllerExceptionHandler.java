package com.gdsc.boilerplate.springboot.exceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gdsc.boilerplate.springboot.utils.ExceptionMessageAccessor;

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
    
}
