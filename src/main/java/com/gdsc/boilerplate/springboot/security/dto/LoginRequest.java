package com.gdsc.boilerplate.springboot.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {

	@NotEmpty(message = "{email_not_empty}")
	private String email;

	@NotEmpty(message = "{password_not_empty}")
	private String password;

}
