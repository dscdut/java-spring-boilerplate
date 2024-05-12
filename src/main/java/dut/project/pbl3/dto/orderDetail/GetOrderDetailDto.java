package dut.project.pbl3.dto.orderDetail;

import dut.project.pbl3.dto.product.GetProductDto;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetOrderDetailDto {

    private Long id;

    private double price;

    private double amount;

    private boolean isCanceled;

    private String cancelReason;

    private GetProductDto product;
}
