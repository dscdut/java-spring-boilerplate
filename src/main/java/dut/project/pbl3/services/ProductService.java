package dut.project.pbl3.services;

import dut.project.pbl3.dto.product.CreateProductDto;
import dut.project.pbl3.dto.product.GetProductDto;
import dut.project.pbl3.dto.product.UpdateProductDto;
import dut.project.pbl3.models.Category;
import dut.project.pbl3.models.Product;
import dut.project.pbl3.repositories.CategoryRepository;
import dut.project.pbl3.repositories.ProductRepository;
import dut.project.pbl3.utils.ObjectMapperUtils;
import dut.project.pbl3.utils.httpResponse.exceptions.DuplicateException;
import dut.project.pbl3.utils.httpResponse.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public List<GetProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return ObjectMapperUtils.mapAll(products, GetProductDto.class);
    }

    public List<GetProductDto> getAllActiveProducts(){
        List<Product> products = productRepository.findAllByDeletedAtIsNull();
        return ObjectMapperUtils.mapAll(products, GetProductDto.class);
    }

    public void createOne(CreateProductDto createProductDto) throws NotFoundException, DuplicateException {
        Optional<Product> foundProduct = this.productRepository.findProductByNameEquals(createProductDto.getName());
        if (foundProduct.isPresent())
            throw new DuplicateException("Product: " + foundProduct.get().getName() + " is existed");

        Product product = new Product();
        if (createProductDto.getIdCategory() != null) {
            Optional<Category> foundCategory = this.categoryRepository.findById(createProductDto.getIdCategory());
            foundCategory.orElseThrow(() -> new NotFoundException("Category ID is invalid"));
            product.setCategory(foundCategory.get());
        }

        if (createProductDto.getImageUrl() != null)
            product.setImageUrl(createProductDto.getImageUrl());


        product.setPrice(createProductDto.getPrice());
        product.setName(createProductDto.getName());

        product.setQuantityRemain(createProductDto.getQuantityRemain());
        product.setUnit(createProductDto.getUnit());
        this.productRepository.save(product);
    }

    public void updateOne(Long id, UpdateProductDto updateProductDto) throws NotFoundException, DuplicateException {
        Optional<Product> foundProduct = this.productRepository.findById(id);
        foundProduct.orElseThrow(() -> new NotFoundException("Not found product"));

        if (!updateProductDto.getName().equals(foundProduct.get().getName())) {
            Optional<Product> foundProductByName = this.productRepository.findProductByNameEquals(updateProductDto.getName());
            if (foundProductByName.isPresent() && foundProductByName.get().getId() != foundProduct.get().getId())
                throw new DuplicateException("Name is used");
        }

        if (updateProductDto.getIdCategory() != null) {
            Optional<Category> foundCategory = this.categoryRepository.findById(updateProductDto.getIdCategory());
            foundCategory.orElseThrow(() -> new NotFoundException("Category ID is invalid"));
            foundProduct.get().setCategory(foundCategory.get());
        }

        if (updateProductDto.getImageUrl() != null)
            foundProduct.get().setImageUrl(updateProductDto.getImageUrl());


        if (Boolean.TRUE.equals(updateProductDto.getIsRestore())) {
            foundProduct.get().setDeletedAt(null);
        }

        foundProduct.get().setName(updateProductDto.getName());
        foundProduct.get().setPrice(updateProductDto.getPrice());
        foundProduct.get().setQuantityRemain(updateProductDto.getQuantityRemain());

        this.productRepository.save(foundProduct.get());
    }

    public void deleteOne(Long productId) throws NotFoundException {
        Optional<Product> foundProduct = this.productRepository.findById(productId);
        foundProduct.orElseThrow(() -> new NotFoundException("Product does not exist"));

        try {
            this.productRepository.deleteById(productId);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            if (foundProduct.get().getDeletedAt() == null) {
                foundProduct.get().setDeletedAt(timestamp);
                this.productRepository.save(foundProduct.get());
            }
        }
    }
}