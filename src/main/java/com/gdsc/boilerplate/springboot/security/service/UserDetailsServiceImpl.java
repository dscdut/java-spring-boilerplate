package com.gdsc.boilerplate.springboot.security.service;

import com.gdsc.boilerplate.springboot.exceptions.InvalidAuthenticationException;
import com.gdsc.boilerplate.springboot.model.User;
import com.gdsc.boilerplate.springboot.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gdsc.boilerplate.springboot.security.dto.UserPrinciple;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		final User authenticatedUser = userRepository.findByEmail(username);

		if (authenticatedUser == null) {
			throw new InvalidAuthenticationException();
		}
		UserPrinciple userPrinciple = UserPrinciple.build(authenticatedUser);
		log.debug("{} is {}", authenticatedUser.getEmail(), userPrinciple.getAuthorities());
		return UserPrinciple.build(authenticatedUser);
	}

	public UserPrinciple loadUserByUserId(Long userId) throws UsernameNotFoundException {
		final User authenticatedUser = userRepository.findById(userId)
				.orElseThrow(() -> new InvalidAuthenticationException());
		
		UserPrinciple userPrinciple = UserPrinciple.build(authenticatedUser);
		log.debug("{} is {}", authenticatedUser.getEmail(), userPrinciple.getAuthorities());
		return userPrinciple;
	}
}
