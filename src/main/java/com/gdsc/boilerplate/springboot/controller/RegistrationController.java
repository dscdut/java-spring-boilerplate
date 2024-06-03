package com.gdsc.boilerplate.springboot.controller;

import com.gdsc.boilerplate.springboot.exceptions.*;
import com.gdsc.boilerplate.springboot.utils.ExceptionMessageAccessor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

import com.gdsc.boilerplate.springboot.security.dto.RegistrationRequest;
import com.gdsc.boilerplate.springboot.security.dto.RegistrationResponse;
import com.gdsc.boilerplate.springboot.security.service.UserService;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegistrationController {

	private final UserService userService;

	@Autowired
	private ExceptionMessageAccessor exceptionMessageAccessor;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> registrationRequest(@Valid @RequestBody RegistrationRequest registrationRequest,  BindingResult bindingResult) {
		if (!registrationRequest.getPassword().equals(registrationRequest.getConfirm_password()) ) {
			throw new InvalidSyntaxException( exceptionMessageAccessor);
		}

		if (bindingResult.hasErrors() && bindingResult.getFieldError().getField().equals("email")) {
			throw new InvalidSyntaxException(exceptionMessageAccessor);
		}
		final RegistrationResponse registrationResponse = userService.registration(registrationRequest);

		return ResponseEntity.status(HttpStatus.CREATED).body(registrationResponse);


	}

}
