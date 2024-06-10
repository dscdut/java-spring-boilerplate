package com.gdsc.boilerplate.springboot.security.dto;

import com.gdsc.boilerplate.springboot.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserResponse {
    private Long id;
    private String full_name;
    private String email;
    private Role role;
}
