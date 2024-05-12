package dut.project.pbl3.dto.product;


import dut.project.pbl3.dto.category.GetCategoryDto;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;

@Validated
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetProductDto {

    @NotNull
    private Long id;

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

    @NotNull
    private String imageUrl;

    @NotNull
    private GetCategoryDto category;

    private String deletedAt;
}