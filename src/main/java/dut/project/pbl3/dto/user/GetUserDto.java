package dut.project.pbl3.dto.user;

import lombok.*;

import javax.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetUserDto {

    @NotNull
    private Long id;

    @Email(message = "is invalid")
    private String email;

    @NotBlank
    private String userName;

    @Pattern(regexp = "ADMIN|MANAGER|USER", message = "is invalid")
    private String role;

    private String deletedAt;
}
