package com.gdsc.boilerplate.springboot.controller;

import com.gdsc.boilerplate.springboot.dto.request.CreateOrderRequest;
import com.gdsc.boilerplate.springboot.security.dto.UserPrinciple;
import com.gdsc.boilerplate.springboot.service.OrderService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @PostMapping
  @PreAuthorize("hasAuthority('MEMBER')")
  public ResponseEntity<?> createOrder(
      @Parameter(hidden = true) @AuthenticationPrincipal UserPrinciple userPrinciple,
      @Valid @RequestBody CreateOrderRequest createOrderRequest)
      throws Exception {
    return ResponseEntity.ok(orderService.createOrder(userPrinciple.getId(), createOrderRequest));
  }

  @PutMapping("/payments/{orderId}/capture")
  @PreAuthorize("hasAuthority('MEMBER')")
  public ResponseEntity<?> updateStatusOrder(
      @Parameter(hidden = true) @AuthenticationPrincipal UserPrinciple userPrinciple,
      @PathVariable Long orderId)
      throws Exception {
    return ResponseEntity.ok(orderService.updateStatusOrder(orderId, userPrinciple.getId()));
  }

  @GetMapping("/payments/{orderId}/capture")
  @PreAuthorize("hasAuthority('MEMBER')")
  public ResponseEntity<?> getOrder(
      @Parameter(hidden = true) @AuthenticationPrincipal UserPrinciple userPrinciple,
      @PathVariable Long orderId) {
    return ResponseEntity.ok(orderService.getOrderById(orderId, userPrinciple.getId()));
  }
}
