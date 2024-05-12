package dut.project.pbl3.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@javax.persistence.Table(name = "order_details")
@Getter
@Setter
public class OrderDetail extends BaseEntity {

    @Column(columnDefinition = "decimal NOT NULL")
    private double price;

    @Column(columnDefinition = "decimal default 0 NOT NULL")
    private double amount;

    @Column(columnDefinition = "boolean default false  NOT NULL")
    private boolean isCanceled;

    @ManyToOne()
    @JoinColumn(name = "id_cancel_user")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;

    @Column
    private String cancelReason;

    @ManyToOne()
    @JoinColumn(name = "id_product")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Product product;

    @ManyToOne()
    @JoinColumn(name = "id_table_order")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private TableOrder tableOrder;
}
