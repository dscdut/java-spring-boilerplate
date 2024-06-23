package com.gdsc.boilerplate.springboot.service;

import java.util.Optional;
import java.util.stream.Collectors;

import com.gdsc.boilerplate.springboot.dto.UpdateUserRequest;
import com.gdsc.boilerplate.springboot.dto.UpdateUserResponse;
import com.gdsc.boilerplate.springboot.dto.UserUpdateInformationRequest;
import com.gdsc.boilerplate.springboot.exceptions.InvalidAuthenticationException;
import com.gdsc.boilerplate.springboot.security.service.RoleService;
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

	@Override
	public User findByEmail(String email) {
		User user = userRepository.findByEmail(email);

		return user;
	}

	@Override
	public RegistrationResponse registration(RegistrationRequest registrationRequest) {

		userValidationService.checkEmail(registrationRequest.getEmail());

		final User user = AuthenticationMapper.INSTANCE.convertToUser(registrationRequest);
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

		final Role role = roleService.findByName("MEMBER");
		final Role roleMap = RoleMapper.INSTANCE.convertToRole(role.getId(), role.getName());

		user.setRole(roleMap);

		try {
			final String fullname = registrationRequest.getFull_name();
			final String email = registrationRequest.getEmail();

			user.setFullname(fullname);
			User savedUser = userRepository.save(user);
			Long userId = savedUser.getId();

			log.info("{} registered successfully!", email);

			return new RegistrationResponse(userId, email, fullname);
		} catch (Exception e) {
			throw new InternalServerException();
		}
	}

	@Override
	public AuthenticatedUserDto findAuthenticatedUserById(Long id) {

		final User user = userRepository.findById(id).orElse(null);

		if (user == null) {
			throw new InvalidAuthenticationException();
		}

		final AuthenticatedUserDto authenticatedUserDto = AuthenticationMapper.INSTANCE
				.convertToAuthenticatedUserDto(user);

		authenticatedUserDto.setRole(user.getRole());

		return authenticatedUserDto;

	}

	@Override
	public AuthenticatedUserDto findAuthenticatedUserByEmail(String email) {
		final User user = findByEmail(email);

		if (user == null) {
			throw new InvalidAuthenticationException();
		}

		final AuthenticatedUserDto authenticatedUserDto = AuthenticationMapper.INSTANCE
				.convertToAuthenticatedUserDto(user);

		authenticatedUserDto.setRole(user.getRole());

		return authenticatedUserDto;
	}

	@Override
	public PageDto<UserDto> getPage(Pageable pageable) {
		Page<User> page = userRepository.findAll(pageable);
		PageDto<UserDto> pageDto = new PageDto<UserDto>();
		pageDto.setTotal(page.getTotalElements());
		pageDto.setData(page.getContent().stream().map(user -> {
			UserDto userDto = UserMapper.INSTANCE.convertToUserDto(user);
			userDto.setFull_name(user.getFullname());
			return userDto;
			})
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
	public User findById(Long id) {
		Optional<User> optionalUser = userRepository.findById(id);

		if (optionalUser.isEmpty()) {
			throw new UserIdNotExistsException();
		}

		return optionalUser.get();
	}

	@Override
	public UpdateUserResponse updateUser(Long id, UpdateUserRequest updateUserRequest) {
		userValidationService.checkEmail(updateUserRequest.getEmail());
		final User userMap = UserMapper.INSTANCE.convertToUser(updateUserRequest);
		userMap.setId(id);

		final User user = findById(id);
		final Role role = roleService.findById(updateUserRequest.getRole_id());

		final Role roleMap = RoleMapper.INSTANCE.convertToRole(updateUserRequest.getRole_id(), role.getName());

		userMap.setRole(roleMap);
		userMap.setPassword(user.getPassword());
		userMap.setFullname(updateUserRequest.getFull_name());
		userRepository.save(userMap);

		return new UpdateUserResponse(userMap.getId(), userMap.getFullname(), userMap.getEmail(), role);
	}

	@Override
	public UpdateUserResponse updateInformationByUser(String email,
			UserUpdateInformationRequest userUpdateInformationRequest) {
		userValidationService.checkEmail(userUpdateInformationRequest.getEmail());
		final User userMap = UserMapper.INSTANCE.convertToUser(userUpdateInformationRequest);
		final User user = findByEmail(email);

		userMap.setId(user.getId());
		userMap.setPassword(user.getPassword());
		userMap.setRole(user.getRole());
		userMap.setFullname(userUpdateInformationRequest.getFull_name());
		userRepository.save(userMap);

		return new UpdateUserResponse(user.getId(), userUpdateInformationRequest.getFull_name(),
				userUpdateInformationRequest.getEmail(), user.getRole());
	}

}
