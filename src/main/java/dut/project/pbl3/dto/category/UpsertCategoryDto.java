package dut.project.pbl3.dto.category;


import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Validated
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpsertCategoryDto {

    @NotNull
    @NotBlank
    private String name;
}
