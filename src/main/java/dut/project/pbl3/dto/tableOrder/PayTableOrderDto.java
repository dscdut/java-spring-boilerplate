package dut.project.pbl3.dto.tableOrder;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;

@Validated
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PayTableOrderDto {

    @Min(0)
    private double discount;

}
