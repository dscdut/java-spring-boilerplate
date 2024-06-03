package com.gdsc.boilerplate.springboot.security.service;

import com.gdsc.boilerplate.springboot.exceptions.InternalServerException;
import com.gdsc.boilerplate.springboot.model.User;
import com.gdsc.boilerplate.springboot.model.UserRole;
import com.gdsc.boilerplate.springboot.repository.UserRepository;
import com.gdsc.boilerplate.springboot.security.dto.AuthenticatedUserDto;
import com.gdsc.boilerplate.springboot.security.dto.RegistrationRequest;
import com.gdsc.boilerplate.springboot.security.dto.RegistrationResponse;
import com.gdsc.boilerplate.springboot.security.mapper.UserMapper;
import com.gdsc.boilerplate.springboot.service.UserValidationService;
import com.gdsc.boilerplate.springboot.utils.ExceptionMessageAccessor;
import com.gdsc.boilerplate.springboot.utils.GeneralMessageAccessor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private static final String REGISTRATION_SUCCESSFUL = "registration_successful";

	private final UserRepository userRepository;

	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	private final UserValidationService userValidationService;

	private final GeneralMessageAccessor generalMessageAccessor;

	@Autowired
	private ExceptionMessageAccessor exceptionMessageAccessor;

	@Override
	public User findByUsername(String username) {

		return userRepository.findByEmail(username);
	}

	@Override
	public RegistrationResponse registration(RegistrationRequest registrationRequest) {

		userValidationService.validateUser(registrationRequest);

		final User user = UserMapper.INSTANCE.convertToUser(registrationRequest);
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setUserRole(UserRole.USER);

		try{
			User savedUser = userRepository.save(user);
			Long userId = savedUser.getId();

			final String username = registrationRequest.getFull_name();
			final String email = registrationRequest.getEmail();

			log.info("{} registered successfully!", username);

			return new RegistrationResponse(userId,email,username);
		}catch (Exception e){
			throw new InternalServerException( exceptionMessageAccessor);
		}
	}

	@Override
	public AuthenticatedUserDto findAuthenticatedUserByUsername(String username) {

		final User user = findByUsername(username);

		return UserMapper.INSTANCE.convertToAuthenticatedUserDto(user);
	}
}
