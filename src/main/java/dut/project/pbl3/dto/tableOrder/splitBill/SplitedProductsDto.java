package dut.project.pbl3.dto.tableOrder.splitBill;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@Validated
public class SplitedProductsDto {

    @NotNull
    private long orderDetailId;

    @NotNull
    private double splitAmount;
}
