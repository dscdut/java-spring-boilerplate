package com.gdsc.boilerplate.springboot.service;

import com.gdsc.boilerplate.springboot.exceptions.EmailAlreadyExistsException;
import com.gdsc.boilerplate.springboot.repository.UserRepository;
import com.gdsc.boilerplate.springboot.security.dto.RegistrationRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserValidationService {

	private final UserRepository userRepository;

	void checkEmail(String email) {

		final boolean existsByEmail = userRepository.existsByEmail(email);

		if (existsByEmail) {

			log.warn("{} is already being used!", email);
			throw new EmailAlreadyExistsException();
		}
	}

}
