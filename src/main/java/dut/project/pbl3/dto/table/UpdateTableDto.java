package dut.project.pbl3.dto.table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Validated
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateTableDto {

    @NotNull
    @NotBlank
    private String name;

    private Long idTableType;

    private Boolean isRestore;
}
