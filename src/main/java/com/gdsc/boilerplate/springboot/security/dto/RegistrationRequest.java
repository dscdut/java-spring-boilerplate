package com.gdsc.boilerplate.springboot.security.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class RegistrationRequest {

	@NotEmpty(message = "{registration_name_not_empty}")
	private String full_name;

	@Email(message = "{registration_email_not_invalid}")
	@NotEmpty(message = "{registration_email_not_empty}")
	private String email;

	@NotEmpty(message = "{registration_password_not_empty}")
	private String password;

	@NotEmpty(message = "{registration_confirm_password_not_empty}")
	private String confirm_password;

}
