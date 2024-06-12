package com.gdsc.boilerplate.springboot.service;

import org.springframework.data.domain.Pageable;

import com.gdsc.boilerplate.springboot.configuration.dto.PageDto;
import com.gdsc.boilerplate.springboot.configuration.dto.user.UserDto;
import com.gdsc.boilerplate.springboot.model.User;
import com.gdsc.boilerplate.springboot.security.dto.*;

import java.util.Optional;

public interface UserService {
	PageDto<UserDto> getPage(Pageable pageable);

	User findByUserName(String username);

	RegistrationResponse registration(RegistrationRequest registrationRequest);

	AuthenticatedUserDto findAuthenticatedUserByUsername(String username);

	void deleteUserById(Long id);

	UpdateUserResponse updateUser(Long id, UpdateUserRequest updateUserRequest);

	UpdateUserResponse updateInformationByUser(String username, UserUpdateInformationRequest userUpdateInformationRequest);

	User findById(Long id);

}
