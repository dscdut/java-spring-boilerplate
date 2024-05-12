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
@javax.persistence.Table(name = "table_type")
@Getter
@Setter
public class TableType extends BaseEntity {

    @Column
    private String name;

    @Column(columnDefinition = "decimal default 0  NOT NULL")
    private double price;

    @OneToMany(mappedBy = "tableType")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Table> tables = new ArrayList<>();
}
