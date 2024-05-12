package dut.project.pbl3.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@javax.persistence.Table(name = "categories")
@Getter
@Setter
public class Category extends BaseEntity {

    @Column
    private String name;

    @OneToMany(mappedBy = "category")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Product> products = new ArrayList<>();
}
