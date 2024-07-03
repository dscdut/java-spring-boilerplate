package com.gdsc.boilerplate.springboot.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.gdsc.boilerplate.springboot.model.User;
import com.gdsc.boilerplate.springboot.security.dto.AuthenticatedUserDto;
import com.gdsc.boilerplate.springboot.security.dto.LoginRequest;
import com.gdsc.boilerplate.springboot.security.dto.LoginResponse;
import com.gdsc.boilerplate.springboot.security.dto.UserPrinciple;
import com.gdsc.boilerplate.springboot.security.mapper.AuthenticationMapper;
import com.gdsc.boilerplate.springboot.service.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenService {

	private final UserService userService;

	private final JwtTokenManager jwtTokenManager;

	private final AuthenticationManager authenticationManager;

	public LoginResponse getLoginResponse(LoginRequest loginRequest) {

		final String email = loginRequest.getEmail();
		final String password = loginRequest.getPassword();

		final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				email, password);

		// Xác thực từ username và password.
		Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

		UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
		final String token = jwtTokenManager.generateToken(userPrinciple);

		log.info("{} has successfully logged in!", userPrinciple.getUsername());

		return new LoginResponse(token);
	}

}
