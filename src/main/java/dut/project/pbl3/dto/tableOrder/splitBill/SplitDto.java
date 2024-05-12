package dut.project.pbl3.dto.tableOrder.splitBill;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Validated
public class SplitDto {

    @NotNull
    private long fromTableOrderId;

    @NotNull
    private long toTableOrderId;

    @NotNull
    private List<SplitedProductsDto> products;
}
