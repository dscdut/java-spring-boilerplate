package dut.project.pbl3.repositories;

import dut.project.pbl3.models.TableType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TableTypeRepository extends JpaRepository<TableType, Long> {

    Optional<TableType> findByNameEquals(String name);
}
