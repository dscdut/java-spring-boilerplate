package dut.project.pbl3.repositories;

import dut.project.pbl3.models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    @Query(
            value = " select * from order_details where id_product = ?1 and id_table_order = ?2 and is_canceled = false limit 1",
            nativeQuery = true
    )
    Optional<OrderDetail> findExistItem(Long idProduct, Long idTableOrder);

    @Transactional
    @Modifying
    @Query(
            value = "Update mvh_01.order_details set mvh_01.order_details.price = ?2, mvh_01.order_details.amount = ?3, " +
                    "mvh_01.order_details.is_canceled = ?4, mvh_01.order_details.cancel_reason = ?5  where id = ?1",
            nativeQuery = true
    )
    void updateOne(Long id, double price, double amount, boolean canceled, String cancelReason);

    @Transactional
    @Modifying
    @Query(
            value = " UPDATE order_details set id_table_order = ?1 where id_table_order = ?2",
            nativeQuery = true
    )
    void setTableOrder(Long newTableOrderId, Long oldTableOrderId);
}
