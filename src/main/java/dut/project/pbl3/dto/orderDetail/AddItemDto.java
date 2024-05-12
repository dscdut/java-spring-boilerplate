package dut.project.pbl3.dto.orderDetail;

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
public class AddItemDto {

    @NotNull
    private Long productId;

    @NotNull
    private Long tableOrderId;
}
