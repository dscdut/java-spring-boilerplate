package com.gdsc.boilerplate.springboot.repository;

import com.gdsc.boilerplate.springboot.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

  PaymentMethod findByName(String name);
}
