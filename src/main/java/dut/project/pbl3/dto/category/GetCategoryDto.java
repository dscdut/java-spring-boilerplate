package dut.project.pbl3.dto.category;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Validated
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetCategoryDto {

    @NotNull
    @Positive
    private Long id;

    @NotNull
    @NotBlank
    private String name;
}
