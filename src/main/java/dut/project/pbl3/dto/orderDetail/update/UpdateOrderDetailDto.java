package dut.project.pbl3.dto.orderDetail.update;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UpdateOrderDetailDto {

    @NotNull
    private Long id;

    private double price;

    private double amount;

    private boolean canceled;

    private String cancelReason;
}
