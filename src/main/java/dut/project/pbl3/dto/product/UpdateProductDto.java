package dut.project.pbl3.dto.product;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;

@Validated
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateProductDto {

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @Positive
    private double price;

    @NotNull
    @PositiveOrZero
    private double quantityRemain;

    @NotNull
    @NotBlank
    private String unit;

    private String imageUrl;

    private Long idCategory;

    private Boolean isRestore;
}
