package com.gdsc.boilerplate.springboot.model;

import com.gdsc.boilerplate.springboot.model.enums.PaymentStatus;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column private String customerName;

  @Column private String customerPhone;

  @Column private Long amount;

  @Column private String currency;

  @Column private String paymentOrderId;

  @Column
  @Enumerated(EnumType.STRING)
  private PaymentStatus paymentStatus;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "payment_method_id")
  private PaymentMethod paymentMethod;
}
