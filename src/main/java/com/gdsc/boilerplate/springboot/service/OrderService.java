package com.gdsc.boilerplate.springboot.service;

import com.gdsc.boilerplate.springboot.dto.request.CreateOrderRequest;
import com.gdsc.boilerplate.springboot.dto.response.CreateOrderResponse;

public interface OrderService {

  CreateOrderResponse createOrder(Long userId, CreateOrderRequest createOrderRequest) throws Exception;
}
