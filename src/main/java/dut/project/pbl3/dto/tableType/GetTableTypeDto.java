package dut.project.pbl3.dto.tableType;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Validated
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetTableTypeDto {

    @NotNull
    private Long id;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    private double price;
}
