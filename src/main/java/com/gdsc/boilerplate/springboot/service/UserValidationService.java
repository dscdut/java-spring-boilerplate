package com.gdsc.boilerplate.springboot.service;

import com.gdsc.boilerplate.springboot.exceptions.CustomException;
import com.gdsc.boilerplate.springboot.exceptions.ExceptionConstants;
import com.gdsc.boilerplate.springboot.repository.UserRepository;
import com.gdsc.boilerplate.springboot.security.dto.RegistrationRequest;
import com.gdsc.boilerplate.springboot.utils.ExceptionMessageAccessor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserValidationService {

	private final UserRepository userRepository;

	private final ExceptionMessageAccessor exceptionMessageAccessor;

	public void validateUser(RegistrationRequest registrationRequest) {

		final String email = registrationRequest.getEmail();

		checkEmail(email);
	}


	private void checkEmail(String email) {

		final boolean existsByEmail = userRepository.existsByEmail(email);

		if (existsByEmail) {

			log.warn("{} is already being used!", email);

			final String existsEmail = exceptionMessageAccessor.getMessage(null, 
					ExceptionConstants.EMAIL_ALREADY_EXISTS.getMessageName());
			
			throw new CustomException(ExceptionConstants.EMAIL_ALREADY_EXISTS.getCode(), existsEmail);
		}
	}

}
