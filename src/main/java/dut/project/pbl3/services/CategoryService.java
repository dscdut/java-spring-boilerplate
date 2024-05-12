package dut.project.pbl3.services;

import dut.project.pbl3.dto.category.UpsertCategoryDto;
import dut.project.pbl3.dto.category.GetCategoryDto;
import dut.project.pbl3.models.Category;
import dut.project.pbl3.repositories.CategoryRepository;
import dut.project.pbl3.repositories.ProductRepository;
import dut.project.pbl3.utils.ObjectMapperUtils;
import dut.project.pbl3.utils.httpResponse.exceptions.BadRequestException;
import dut.project.pbl3.utils.httpResponse.exceptions.DuplicateException;
import dut.project.pbl3.utils.httpResponse.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public GetCategoryDto createOne(UpsertCategoryDto upsertCategoryDto) throws BadRequestException, DuplicateException {
        Optional<Category> foundCategory = this.categoryRepository.findByNameEquals(upsertCategoryDto.getName());
        if (foundCategory.isPresent())
            throw new DuplicateException("Category is existed");

        Category category = ObjectMapperUtils.map(upsertCategoryDto, Category.class);
        Category createdOne = this.categoryRepository.save(category);

        return ObjectMapperUtils.map(createdOne, GetCategoryDto.class);
    }

    public void updateOne(Long id, UpsertCategoryDto upsertCategoryDto) throws NotFoundException, DuplicateException {
        Optional<Category> foundCategory = this.categoryRepository.findById(id);
        foundCategory.orElseThrow(() -> new NotFoundException("Not found Category"));

        Optional<Category> foundCategoryName = this.categoryRepository.findByNameEquals(upsertCategoryDto.getName());
        if (foundCategoryName.isPresent() && foundCategoryName.get().getId() != id)
            throw new DuplicateException("This name is used");

        foundCategory.get().setName(upsertCategoryDto.getName());

        this.categoryRepository.save(foundCategory.get());
    }

    public void deleteOne(Long id) throws NotFoundException {
        Optional<Category> foundCategory = this.categoryRepository.findById(id);
        foundCategory.orElseThrow(() -> new NotFoundException("Not found Category"));

        foundCategory.get().getProducts().forEach(product -> {
            product.setCategory(null);
            this.productRepository.save(product);
        });

        this.categoryRepository.delete(foundCategory.get());
    }

    public List<GetCategoryDto> getAllCategory() {
        List<Category> categories = this.categoryRepository.findAll();
        return ObjectMapperUtils.mapAll(categories, GetCategoryDto.class);
    }
}
