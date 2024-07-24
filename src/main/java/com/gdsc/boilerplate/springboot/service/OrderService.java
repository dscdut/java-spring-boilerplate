package com.gdsc.boilerplate.springboot.service;

import com.gdsc.boilerplate.springboot.dto.request.CreateOrderRequest;
import com.gdsc.boilerplate.springboot.dto.response.CreateOrderResponse;
import com.gdsc.boilerplate.springboot.dto.response.OrderInfoResponse;
import com.gdsc.boilerplate.springboot.dto.response.PaymentStatusResponse;

public interface OrderService {

  CreateOrderResponse createOrder(Long userId, CreateOrderRequest createOrderRequest)
      throws Exception;

  OrderInfoResponse getOrderById(Long orderId, Long userId);

  PaymentStatusResponse updateStatusOrder(Long orderId, Long userId) throws Exception;
}
