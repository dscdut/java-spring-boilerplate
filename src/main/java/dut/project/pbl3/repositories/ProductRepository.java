package dut.project.pbl3.repositories;

import dut.project.pbl3.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findProductByNameEquals(String name);

    List<Product> findAllByDeletedAtIsNull();
}