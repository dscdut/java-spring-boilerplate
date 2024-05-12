package dut.project.pbl3.dto.tableType;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;

@Validated
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateTableTypeDto {

    @NotNull
    @NotBlank
    private String name;

    @DecimalMin("0.0")
    private double price;
}
