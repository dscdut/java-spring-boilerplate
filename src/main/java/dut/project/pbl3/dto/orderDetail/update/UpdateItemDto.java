package dut.project.pbl3.dto.orderDetail.update;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UpdateItemDto {
    private List<UpdateOrderDetailDto> orderDetails;
}
