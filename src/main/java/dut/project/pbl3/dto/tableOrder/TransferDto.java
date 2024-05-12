package dut.project.pbl3.dto.tableOrder;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransferDto {

    @NotNull
    private Long billId;

    @NotNull
    private Long tableId;
}
