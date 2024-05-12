package dut.project.pbl3.dto.table;

import dut.project.pbl3.dto.tableType.GetTableTypeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetTableDto {

    @NotNull
    private Long id;

    @NotBlank
    private String name;

    private GetTableTypeDto tableType;

    @NotBlank
    private boolean status;

    private String deletedAt;
}
