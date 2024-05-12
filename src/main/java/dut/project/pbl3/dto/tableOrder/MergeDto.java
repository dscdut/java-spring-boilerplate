package dut.project.pbl3.dto.tableOrder;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MergeDto {

    @NotNull
    private Long fromBill;

    @NotEmpty
    private List<Long> toBill;
}
