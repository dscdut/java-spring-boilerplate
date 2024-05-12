package dut.project.pbl3.dto.table;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Validated
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateTableDto {

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    private Long idTableType;
}
