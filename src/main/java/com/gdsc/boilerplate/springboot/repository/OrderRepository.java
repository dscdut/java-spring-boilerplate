package com.gdsc.boilerplate.springboot.repository;

import com.gdsc.boilerplate.springboot.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {}
