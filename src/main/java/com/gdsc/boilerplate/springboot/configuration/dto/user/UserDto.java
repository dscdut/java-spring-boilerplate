package com.gdsc.boilerplate.springboot.configuration.dto.user;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {
	private Integer id;
	
	private String full_name;

	private String email;
	
}
