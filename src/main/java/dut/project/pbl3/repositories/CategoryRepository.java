package dut.project.pbl3.repositories;

import dut.project.pbl3.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByNameEquals(String name);
}
