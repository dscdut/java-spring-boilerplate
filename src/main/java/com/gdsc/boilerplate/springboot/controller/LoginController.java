package com.gdsc.boilerplate.springboot.controller;

import com.gdsc.boilerplate.springboot.exceptions.InternalServerException;
import com.gdsc.boilerplate.springboot.exceptions.InvalidAuthenticationException;
import com.gdsc.boilerplate.springboot.exceptions.InvalidSyntaxLoginException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.gdsc.boilerplate.springboot.security.dto.LoginRequest;
import com.gdsc.boilerplate.springboot.security.dto.LoginResponse;
import com.gdsc.boilerplate.springboot.security.jwt.JwtTokenService;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

	private final JwtTokenService jwtTokenService;

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

}
