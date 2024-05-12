package dut.project.pbl3.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@javax.persistence.Table(name = "tables")
@Getter
@Setter
@ToString
public class Table extends BaseEntity {

    @Column
    private String name;

    @Column(columnDefinition = "boolean default false  NOT NULL")
    private boolean status;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_table_type")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private TableType tableType;

    @OneToMany(mappedBy = "table")
    private List<TableOrder> tableOrders = new ArrayList<>();
}
