package com.gdsc.boilerplate.springboot.security.service;

import com.gdsc.boilerplate.springboot.model.User;
import com.gdsc.boilerplate.springboot.security.dto.AuthenticatedUserDto;
import com.gdsc.boilerplate.springboot.security.dto.RegistrationRequest;
import com.gdsc.boilerplate.springboot.security.dto.RegistrationResponse;

public interface UserService {

	User findByUsername(String username);

	RegistrationResponse registration(RegistrationRequest registrationRequest);

	AuthenticatedUserDto findAuthenticatedUserByUsername(String username);

}
