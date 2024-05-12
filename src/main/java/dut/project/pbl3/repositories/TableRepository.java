package dut.project.pbl3.repositories;

import dut.project.pbl3.models.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface TableRepository extends JpaRepository<Table, Long> {

    Optional<Table> findByNameEquals(String name);

    List<Table> findAllByDeletedAtIsNull();

    @Transactional
    @Modifying
    @Query(
            value = " Update mvh_01.tables set mvh_01.tables.status = 0 where mvh_01.tables.id = ?1",
            nativeQuery = true
    )
    void setFreeStatus(Long tableId);

    @Transactional
    @Modifying
    @Query(
            value = "Update mvh_01.tables set mvh_01.tables.deleted_at = NULL where id = ?1",
            nativeQuery = true
    )
    void restoreTable(Long id);
}
