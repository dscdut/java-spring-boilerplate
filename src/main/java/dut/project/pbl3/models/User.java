package dut.project.pbl3.models;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@javax.persistence.Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User extends BaseEntity {

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String userName;

    @Column
    private String password;

    @Column
    private String role;

    @OneToMany(mappedBy = "user")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<TableOrder> tableOrders = new ArrayList<>();

    public User(String email, String userName, String password, String role) {
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.role = role;
    }
}
