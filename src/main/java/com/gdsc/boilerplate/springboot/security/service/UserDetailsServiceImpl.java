package com.gdsc.boilerplate.springboot.security.service;

import com.gdsc.boilerplate.springboot.model.Role;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserService userService;

	private final RoleService roleService;

	@Override
	public UserDetails loadUserByUsername(String email) {

		final AuthenticatedUserDto authenticatedUser = userService.findAuthenticatedUserByUsername(email);

		final String authenticatedUsername = authenticatedUser.getEmail();
		final String authenticatedPassword = authenticatedUser.getPassword();

		final Role role = roleService.findById(authenticatedUser.getRole().getId());

		final SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getName());
		log.debug("{} is {}", authenticatedUsername ,grantedAuthority);
		return new User(authenticatedUsername, authenticatedPassword, Collections.singletonList(grantedAuthority));
	}
}
