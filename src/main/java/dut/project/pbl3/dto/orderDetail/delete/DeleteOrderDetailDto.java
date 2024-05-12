package dut.project.pbl3.dto.orderDetail.delete;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Validated
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DeleteOrderDetailDto {
    @NotNull
    @NotBlank
    private String reason;
}
