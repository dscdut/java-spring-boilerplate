package dut.project.pbl3.controllers;

import com.google.gson.Gson;
import dut.project.pbl3.dto.product.CreateProductDto;
import dut.project.pbl3.dto.product.GetProductDto;
import dut.project.pbl3.dto.product.UpdateProductDto;
import dut.project.pbl3.services.ProductService;
import dut.project.pbl3.utils.httpResponse.NoContentResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("products")
public class ProductController {

    private final ProductService productService;

    @GetMapping("")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public List<GetProductDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/active")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public List<GetProductDto> getAllActiveProducts() {
        return productService.getAllActiveProducts();
    }


    @SneakyThrows
    @PostMapping("")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String createOne(@Valid @RequestBody CreateProductDto createProductDto) {
        this.productService.createOne(createProductDto);
        return new Gson().toJson(new NoContentResponse());
    }


    @SneakyThrows
    @PutMapping("/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String updateOne(@Valid @RequestBody UpdateProductDto updateProductDto,
                            @PathVariable("id") Long id) {
        this.productService.updateOne(id, updateProductDto);
        return new Gson().toJson(new NoContentResponse());
    }

    @SneakyThrows
    @DeleteMapping("/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String deleteOne(@PathVariable("id") Long id) {
        this.productService.deleteOne(id);
        return new Gson().toJson(new NoContentResponse());
    }
}
