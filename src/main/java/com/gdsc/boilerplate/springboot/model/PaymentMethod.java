package com.gdsc.boilerplate.springboot.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payment_methods")
public class PaymentMethod extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column private String name;
}
