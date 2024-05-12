package dut.project.pbl3.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;

@Validated
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UpdateUserDto {

    @NotNull
    @NotBlank
    @Email(message = "Email is invalid")
    private String email;

    @NotNull
    @NotBlank
    private String userName;

    @Length(min = 6, max = 30, message = "Password length must be more than 6 digits")
    private String password;

    @Length(min = 6, max = 30)
    private String confirmPassword;

    @NotNull
    @Pattern(regexp = "ADMIN|MANAGER|STAFF", message = "is invalid")
    private String role;

    private Boolean isRestore;
}
