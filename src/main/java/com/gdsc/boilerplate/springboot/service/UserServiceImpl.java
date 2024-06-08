package com.gdsc.boilerplate.springboot.service;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.gdsc.boilerplate.springboot.configuration.dto.PageDto;
import com.gdsc.boilerplate.springboot.configuration.dto.user.UserDto;
import com.gdsc.boilerplate.springboot.exceptions.InternalServerException;
import com.gdsc.boilerplate.springboot.exceptions.UserIdNotExistsException;
import com.gdsc.boilerplate.springboot.maper.user.UserMapper;
import com.gdsc.boilerplate.springboot.model.User;
import com.gdsc.boilerplate.springboot.model.UserRole;
import com.gdsc.boilerplate.springboot.repository.UserRepository;
import com.gdsc.boilerplate.springboot.security.dto.AuthenticatedUserDto;
import com.gdsc.boilerplate.springboot.security.dto.RegistrationRequest;
import com.gdsc.boilerplate.springboot.security.dto.RegistrationResponse;
import com.gdsc.boilerplate.springboot.security.mapper.AuthenticationMapper;
import com.gdsc.boilerplate.springboot.utils.GeneralMessageAccessor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private static final String REGISTRATION_SUCCESSFUL = "registration_successful";

	private final UserRepository userRepository;

	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	private final UserValidationService userValidationService;

	private final GeneralMessageAccessor generalMessageAccessor;

	@Override
	public User findByUsername(String username) {

		return userRepository.findByEmail(username);
	}

	@Override
	public RegistrationResponse registration(RegistrationRequest registrationRequest) {

		userValidationService.validateUser(registrationRequest);

		final User user = AuthenticationMapper.INSTANCE.convertToUser(registrationRequest);
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setUserRole(UserRole.USER);

		try {
			User savedUser = userRepository.save(user);
			Long userId = savedUser.getId();

			final String username = registrationRequest.getFull_name();
			final String email = registrationRequest.getEmail();

			log.info("{} registered successfully!", username);

			return new RegistrationResponse(userId, email, username);
		} catch (Exception e) {
			throw new InternalServerException();
		}
	}

	@Override
	public AuthenticatedUserDto findAuthenticatedUserByUsername(String username) {

		final User user = findByUsername(username);

		return AuthenticationMapper.INSTANCE.convertToAuthenticatedUserDto(user);
	}

	@Override
	public PageDto<UserDto> getPage(Pageable pageable) {
		Page<User> page = userRepository.findAll(pageable);
		PageDto<UserDto> pageDto = new PageDto<UserDto>();
		pageDto.setTotal(page.getTotalElements());
		pageDto.setData(page.getContent().stream().map(user -> UserMapper.INSTANCE.convertToUserDto(user))
				.collect(Collectors.toList()));
		return pageDto;
	}

	@Override
	public void deleteUserById(Long id) {
		final Optional<User> optionalUser = userRepository.findById(id);

		if (optionalUser.isEmpty()) {
			throw new UserIdNotExistsException();
		}

		try {
			userRepository.deleteById(id);
		} catch (Exception e) {
			throw new InternalServerException();
		}
	}
}
