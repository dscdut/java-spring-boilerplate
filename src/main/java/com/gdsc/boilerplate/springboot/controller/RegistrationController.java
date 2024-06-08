package com.gdsc.boilerplate.springboot.controller;

import com.gdsc.boilerplate.springboot.exceptions.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gdsc.boilerplate.springboot.security.dto.RegistrationRequest;
import com.gdsc.boilerplate.springboot.security.dto.RegistrationResponse;
import com.gdsc.boilerplate.springboot.service.UserService;
import com.gdsc.boilerplate.springboot.utils.ExceptionMessageAccessor;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
@Slf4j
public class RegistrationController {

	private final UserService userService;

	private final ExceptionMessageAccessor accessor;
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RegistrationResponse> registrationRequest(@Valid @RequestBody RegistrationRequest registrationRequest) {
		if (!registrationRequest.getPassword().equals(registrationRequest.getConfirm_password())) {
			throw new InvalidSyntaxException();
		}

		final RegistrationResponse registrationResponse = userService.registration(registrationRequest);

		return ResponseEntity.status(HttpStatus.CREATED).body(registrationResponse);

	}
	

	@ExceptionHandler(EmailAlreadyExistsException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	ResponseEntity<ApiExceptionResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException exception) {

		final ApiExceptionResponse response = new ApiExceptionResponse(
				ExceptionConstants.EMAIL_ALREADY_EXISTS.getCode(),
				accessor.getMessage(null, ExceptionConstants.EMAIL_ALREADY_EXISTS.getMessageName()));
		log.warn("EmailAlreadyExistsException: {}", response.getMessage());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}

}
