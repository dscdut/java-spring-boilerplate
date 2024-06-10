package com.gdsc.boilerplate.springboot.security.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class RegistrationRequest {


	@Pattern(regexp = "^[a-zA-Z]+([ '-][a-zA-Z]+)*$", message = "{fullname_invalid}")
	@NotEmpty(message = "{fullname_not_empty}")
	@Schema( example = "string")
	private String full_name;


	@NotEmpty(message = "{email_not_empty}")
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "{email_invalid}")
	@Schema( example = "string")
	private String email;


	@Pattern(regexp = "^(?=.*[0-9])(?=.*[A-Z]).{7,50}$", message = "{password_invalid}")
	@NotEmpty(message = "{password_not_empty}")
	@Schema( example = "string")
	private String password;

	@NotEmpty(message = "{registration_confirm_password_not_empty}")
	private String confirm_password;

}
