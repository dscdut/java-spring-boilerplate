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
	//@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "{email_invalid}")
	@Schema( example = "string")
	private String email;

	//@Pattern(regexp = "^(?=.*[0-9])(?=.*[A-Z]).{7,50}$", message = "{password_invalid}")
	@NotEmpty(message = "{password_not_empty}")
	@Schema( example = "string")
	private String password;

}
