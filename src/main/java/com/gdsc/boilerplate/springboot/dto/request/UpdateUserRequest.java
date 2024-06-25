package com.gdsc.boilerplate.springboot.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UpdateUserRequest {
    @Pattern(regexp = "^[a-zA-Z]+([ '-][a-zA-Z]+)*$", message = "{fullname_invalid}")
    @NotEmpty(message = "{fullname_not_empty}")
    @Schema( example = "string")
    private String full_name;

    @NotEmpty(message = "{email_not_empty}")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "{email_invalid}")
    @Schema( example = "string")
    private String email;

    @NotNull(message = "role_id_not_empty")
    private Long role_id;

}
