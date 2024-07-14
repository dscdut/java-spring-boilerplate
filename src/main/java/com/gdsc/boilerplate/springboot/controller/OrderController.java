package com.gdsc.boilerplate.springboot.controller;

import com.gdsc.boilerplate.springboot.dto.request.CreateOrderRequest;
import com.gdsc.boilerplate.springboot.security.dto.UserPrinciple;
import com.gdsc.boilerplate.springboot.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @PostMapping
  @PreAuthorize("hasAuthority('MEMBER')")
  public ResponseEntity<?> createOrder(
      @AuthenticationPrincipal UserPrinciple userPrinciple,
      @Valid @RequestBody CreateOrderRequest createOrderRequest) throws Exception {
    return ResponseEntity.ok(orderService.createOrder(userPrinciple.getId(), createOrderRequest));
  }
}
