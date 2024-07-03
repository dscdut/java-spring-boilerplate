package com.gdsc.boilerplate.springboot.security.dto;

import com.gdsc.boilerplate.springboot.model.Role;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthenticatedUserDto {
	
	private Long id;
	
	private String fullname;

	private String email;

	private String password;

	private Role role;


}
