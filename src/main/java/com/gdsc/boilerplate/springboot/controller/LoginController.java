package com.gdsc.boilerplate.springboot.controller;

import com.gdsc.boilerplate.springboot.exceptions.ApiExceptionResponse;
import com.gdsc.boilerplate.springboot.exceptions.ExceptionConstants;
import com.gdsc.boilerplate.springboot.exceptions.InternalServerException;
import com.gdsc.boilerplate.springboot.exceptions.InvalidAuthenticationException;
import com.gdsc.boilerplate.springboot.exceptions.InvalidSyntaxLoginException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.gdsc.boilerplate.springboot.security.dto.LoginRequest;
import com.gdsc.boilerplate.springboot.security.dto.LoginResponse;
import com.gdsc.boilerplate.springboot.security.jwt.JwtTokenService;
import com.gdsc.boilerplate.springboot.utils.ExceptionMessageAccessor;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
@Slf4j
public class LoginController {

	private final JwtTokenService jwtTokenService;
	
	private final ExceptionMessageAccessor accessor;
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LoginResponse> loginRequest(@Valid @RequestBody LoginRequest loginRequest,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new InvalidSyntaxLoginException();
		}
		try {
			final LoginResponse loginResponse = jwtTokenService.getLoginResponse(loginRequest);
			return ResponseEntity.ok(loginResponse);
		} catch (BadCredentialsException e) {
			throw new InvalidAuthenticationException();
		} catch (Exception e) {
			throw new InternalServerException();
		}
	}


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
