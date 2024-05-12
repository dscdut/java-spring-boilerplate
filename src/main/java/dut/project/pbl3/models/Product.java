package dut.project.pbl3.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@javax.persistence.Table(name = "products")
@Getter
@Setter
public class Product extends BaseEntity {

    @Column
    private String name;

    @Column(columnDefinition = "decimal default 0  NOT NULL")
    private double price;

    @Column(columnDefinition = "decimal default 0 NOT NULL")
    private double quantityRemain;

    @Column
    private String unit;

    @Column(columnDefinition = "varchar(255) default 'https://cdn-pos.kiotviet.vn/kiotvietpos/fnb/static/assets/img/default-product.svg'")
    private String imageUrl = "https://cdn-pos.kiotviet.vn/kiotvietpos/fnb/static/assets/img/default-product.svg";

    @ManyToOne()
    @JoinColumn(name = "id_category")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Category category;

    @OneToMany(mappedBy = "product")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<OrderDetail> orderDetails = new ArrayList<>();
}
