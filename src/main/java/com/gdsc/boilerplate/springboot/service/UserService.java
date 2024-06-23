package com.gdsc.boilerplate.springboot.service;

import com.gdsc.boilerplate.springboot.dto.UpdateUserRequest;
import com.gdsc.boilerplate.springboot.dto.UpdateUserResponse;
import com.gdsc.boilerplate.springboot.dto.UserUpdateInformationRequest;
import org.springframework.data.domain.Pageable;

import com.gdsc.boilerplate.springboot.configuration.dto.PageDto;
import com.gdsc.boilerplate.springboot.configuration.dto.user.UserDto;
import com.gdsc.boilerplate.springboot.model.User;
import com.gdsc.boilerplate.springboot.security.dto.*;

public interface UserService {
	PageDto<UserDto> getPage(Pageable pageable);

	User findByEmail(String email);

	RegistrationResponse registration(RegistrationRequest registrationRequest);

	AuthenticatedUserDto findAuthenticatedUserById(Long id);

	AuthenticatedUserDto findAuthenticatedUserByEmail(String email);

	void deleteUserById(Long id);

	UpdateUserResponse updateUser(Long id, UpdateUserRequest updateUserRequest);

	UpdateUserResponse updateInformationByUser(String username,
			UserUpdateInformationRequest userUpdateInformationRequest);

	User findById(Long id);

}
