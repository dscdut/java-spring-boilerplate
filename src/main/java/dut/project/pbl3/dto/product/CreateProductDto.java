package dut.project.pbl3.dto.product;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Validated
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateProductDto {

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
}