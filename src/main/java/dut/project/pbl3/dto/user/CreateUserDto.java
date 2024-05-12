package dut.project.pbl3.dto.user;

import dut.project.pbl3.utils.PasswordsEqualConstraint;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;

@Validated
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@PasswordsEqualConstraint(message = "Confirmed password does not match")
public class CreateUserDto {

    @NotNull
    @NotBlank
    @Email(message = "Email is invalid")
    private String email;

    @NotNull
    @NotBlank
    private String userName;

    @NotNull
    @NotBlank
    @Length(min = 6, max = 30, message = "Password length must be more than 6 digits")
    private String password;

    @NotNull
    @NotBlank
    @Length(min = 6, max = 30)
    private String confirmPassword;

    @NotNull
    @Pattern(regexp = "ADMIN|MANAGER|STAFF", message = "is invalid")
    private String role;
}
