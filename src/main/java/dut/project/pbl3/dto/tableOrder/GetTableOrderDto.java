package dut.project.pbl3.dto.tableOrder;

import dut.project.pbl3.dto.orderDetail.GetOrderDetailDto;
import dut.project.pbl3.dto.user.GetUserDto;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetTableOrderDto {
    private Long id;

    private String beginAt;

    private String endAt;

    private long discount;

    private boolean isPaid;

    private boolean isCanceled;

    private List<GetOrderDetailDto> orderDetails;

    private Long idTable;

    private double tablePrice;

    private double total;

    private String cancelReason;

    private GetUserDto user;

    private String updatedAt;
}
