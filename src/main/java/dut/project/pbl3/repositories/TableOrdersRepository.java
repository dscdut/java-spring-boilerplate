package dut.project.pbl3.repositories;

import dut.project.pbl3.models.Table;
import dut.project.pbl3.models.TableOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface TableOrdersRepository extends JpaRepository<TableOrder, Long> {

    List<TableOrder> findByIsCanceledFalseAndIsPaidFalse();

    List<TableOrder> findByTableAndIsCanceledFalseAndIsPaidFalse(Table table);

    List<TableOrder> findByTableIdAndIsCanceledFalseAndIsPaidFalse(Long id);

    List<TableOrder> findAllByDeletedAtIsNullAndIsPaidIsTrueOrIsCanceledIsTrue();

    Optional<TableOrder> findByIdAndIsCanceledFalseAndIsPaidFalse(Long id);

    @Query(
            value = "SELECT * FROM mvh_01.table_orders WHERE DATE(mvh_01.table_orders.begin_at) = ?1 " +
                    "AND is_paid = 1",
            nativeQuery = true
    )
    List<TableOrder> findAllOrderByBeginAtAndIsPaid(String date);

    @Transactional
    @Modifying
    void deleteTableOrderById(long id);

}
