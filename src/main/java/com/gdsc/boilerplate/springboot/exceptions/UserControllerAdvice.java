package com.gdsc.boilerplate.springboot.exceptions;

import com.gdsc.boilerplate.springboot.controller.UserController;
import com.gdsc.boilerplate.springboot.utils.ExceptionMessageAccessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackageClasses = UserController.class)
public class UserControllerAdvice {
    @Autowired
    private ExceptionMessageAccessor accessor;

    @ExceptionHandler(UserIdNotExistsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ResponseEntity<ApiExceptionResponse> handleUserIdNotExistsException(UserIdNotExistsException exception) {

        final ApiExceptionResponse response = new ApiExceptionResponse(
                ExceptionConstants.USER_ID_NOT_EXISTS.getCode(),
                accessor.getMessage(null, ExceptionConstants.USER_ID_NOT_EXISTS.getMessageName()));
        log.warn("UserIdNotExistsException: {}", response.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
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
