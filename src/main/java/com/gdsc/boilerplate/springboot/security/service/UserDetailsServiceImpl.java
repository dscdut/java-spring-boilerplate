package com.gdsc.boilerplate.springboot.security.service;

import com.gdsc.boilerplate.springboot.exceptions.InvalidAuthenticationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.gdsc.boilerplate.springboot.security.dto.AuthenticatedUserDto;
import com.gdsc.boilerplate.springboot.service.UserService;

import java.util.Collections;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserService userService;

	@Override
	public UserDetails loadUserByUsername(String email) {

		final AuthenticatedUserDto authenticatedUser = userService.findAuthenticatedUserByUsername(email);

		final String authenticatedUsername = authenticatedUser.getEmail();
		final String authenticatedPassword = authenticatedUser.getPassword();

		String userRole = "USER";

		if(authenticatedUser.getRole().getId() == 1){
			userRole = "ADMIN";
		}

		final SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(userRole);
		log.debug("{} is {}", authenticatedUsername ,grantedAuthority);
		return new User(authenticatedUsername, authenticatedPassword, Collections.singletonList(grantedAuthority));
	}
}
