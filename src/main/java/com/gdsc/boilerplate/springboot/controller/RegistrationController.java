package com.gdsc.boilerplate.springboot.controller;

import com.gdsc.boilerplate.springboot.exceptions.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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


	@SneakyThrows
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RegistrationResponse> registrationRequest(@Valid @RequestBody RegistrationRequest registrationRequest,
			BindingResult bindingResult) {
		if (!registrationRequest.getPassword().equals(registrationRequest.getConfirm_password())) {
			throw new MethodArgumentNotValidException(null, bindingResult);
		}

		if (bindingResult.hasErrors()) {
			throw new MethodArgumentNotValidException(null, bindingResult);
		}
		final RegistrationResponse registrationResponse = userService.registration(registrationRequest);

		return ResponseEntity.status(HttpStatus.CREATED).body(registrationResponse);

	}

}
