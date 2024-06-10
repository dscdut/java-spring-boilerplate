package com.gdsc.boilerplate.springboot.service;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.gdsc.boilerplate.springboot.exceptions.InvalidAuthenticationException;
import com.gdsc.boilerplate.springboot.exceptions.RoleIdNotExistsException;
import com.gdsc.boilerplate.springboot.security.service.RoleService;
import com.gdsc.boilerplate.springboot.utils.ExceptionMessageAccessor;
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
import com.gdsc.boilerplate.springboot.repository.UserRepository;
import com.gdsc.boilerplate.springboot.security.dto.AuthenticatedUserDto;
import com.gdsc.boilerplate.springboot.security.dto.RegistrationRequest;
import com.gdsc.boilerplate.springboot.security.dto.RegistrationResponse;
import com.gdsc.boilerplate.springboot.security.mapper.AuthenticationMapper;
import com.gdsc.boilerplate.springboot.model.Role;
import com.gdsc.boilerplate.springboot.security.dto.*;
import com.gdsc.boilerplate.springboot.security.mapper.RoleMapper;

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

	private final RoleService roleService;

	final private ExceptionMessageAccessor accessor;

	@Override
	public User findByUserName(String username) {

		return userRepository.findByEmail(username);
	}

	@Override
	public RegistrationResponse registration(RegistrationRequest registrationRequest) {

		userValidationService.validateUser(registrationRequest);

		final User user = AuthenticationMapper.INSTANCE.convertToUser(registrationRequest);
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));


		final Role role = RoleMapper.INSTANCE.convertToRole(Long.parseLong("2"), "USER");

		user.setRole(role);

		try{
			final String username = registrationRequest.getFull_name();
			final String email = registrationRequest.getEmail();

			user.setName(username);
			User savedUser = userRepository.save(user);
			Long userId = savedUser.getId();

			log.info("{} registered successfully!", username);

			return new RegistrationResponse(userId, email, username);
		} catch (Exception e) {
			throw new InternalServerException();
		}
	}

	@Override
	public AuthenticatedUserDto findAuthenticatedUserByUsername(String username) {

		final User user = findByUserName(username);
		final AuthenticatedUserDto authenticatedUserDto = AuthenticationMapper.INSTANCE.convertToAuthenticatedUserDto(user);

		if (Objects.isNull(authenticatedUserDto)) {
			throw new InvalidAuthenticationException();
		}

		authenticatedUserDto.setRole(user.getRole());

		return authenticatedUserDto;

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

	@Override
	public Optional<User> findById(Long id) {

		return userRepository.findById(id);
	}

	@Override
	public UpdateUserResponse updateUser(Long id, UpdateUserRequest updateUserRequest) {
		final User user = UserMapper.INSTANCE.convertToUser(updateUserRequest);
		user.setId(id);

		final Optional<User> optionalUser  = findById(id);
		final Optional<Role> optionalRole  =  roleService.findById(updateUserRequest.getRole_id());

		if (optionalUser.isEmpty()) {
			throw new UserIdNotExistsException();
		}

		if (optionalRole.isEmpty()) {
			throw new RoleIdNotExistsException();
		}

		final Role role = RoleMapper.INSTANCE.convertToRole(updateUserRequest.getRole_id(), optionalRole.get().getName());
		user.setRole(role);
		user.setPassword(optionalUser.get().getPassword());
		user.setName(updateUserRequest.getFull_name());
		userRepository.save(user);

		return new UpdateUserResponse(user.getId(), user.getName(), user.getEmail(), role);
	}

	@Override
	public UpdateUserResponse updateInformationByUser(String username, UserUpdateInformationRequest userUpdateInformationRequest) {
		final User userMap = UserMapper.INSTANCE.convertToUser(userUpdateInformationRequest);
		final User user = findByUserName(username);

		if (user == null) {
			throw new UserIdNotExistsException();
		}
		userMap.setId(user.getId());
		userMap.setPassword(user.getPassword());
		userMap.setRole(user.getRole());
		userRepository.save(userMap);


		return new UpdateUserResponse(user.getId(),userUpdateInformationRequest.getFull_name(), userUpdateInformationRequest.getEmail(), user.getRole());
	}

}
