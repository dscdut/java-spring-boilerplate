package dut.project.pbl3.controllers;

import com.google.gson.Gson;
import dut.project.pbl3.dto.category.GetCategoryDto;
import dut.project.pbl3.dto.category.UpsertCategoryDto;
import dut.project.pbl3.services.CategoryService;
import dut.project.pbl3.utils.httpResponse.NoContentResponse;
import dut.project.pbl3.utils.httpResponse.okResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
@Slf4j
@RequestMapping("categories")
@PreAuthorize("hasAnyRole('ADMIN')")
public class CategoryController {

    private final CategoryService categoryService;

    @SneakyThrows
    @PostMapping("")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String insertOne(@Valid @RequestBody UpsertCategoryDto upsertCategoryDto) {
        GetCategoryDto getCategoryDto = this.categoryService.createOne(upsertCategoryDto);
        return new Gson().toJson(new okResponse(getCategoryDto));
    }

    @SneakyThrows
    @PutMapping("/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String updateOne(@Valid @RequestBody UpsertCategoryDto upsertCategoryDto,
                            @PathVariable Long id) {
        this.categoryService.updateOne(id, upsertCategoryDto);
        return new Gson().toJson(new NoContentResponse());
    }

    @SneakyThrows
    @DeleteMapping("/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String deleteOne(@PathVariable Long id) {
        this.categoryService.deleteOne(id);
        return new Gson().toJson(new NoContentResponse());
    }
}
