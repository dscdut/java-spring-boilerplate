package dut.project.pbl3.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@javax.persistence.Table(name = "table_orders")
@Getter
@Setter
@ToString
public class TableOrder extends BaseEntity {

    @Column(columnDefinition = "int default 0  NOT NULL")
    public double discount;

    @CreationTimestamp
    public java.sql.Timestamp beginAt;

    @Column
    public java.sql.Timestamp endAt;

    @Column(columnDefinition = "boolean default false  NOT NULL")
    public boolean isPaid;

    @Column(columnDefinition = "decimal default 0  NOT NULL")
    public double total;

    @Column(columnDefinition = "decimal NOT NULL")
    public double tablePrice;

    @Column(columnDefinition = "boolean default false NOT NULL")
    public boolean isCanceled;

    @ManyToOne()
    @JoinColumn(name = "id_cancel_user")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;

    @Column
    public String cancelReason;

    @OneToMany(mappedBy = "tableOrder")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    public List<OrderDetail> orderDetails = new ArrayList<>();

    @ManyToOne()
    @JoinColumn(name = "id_table")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    public Table table;
}
