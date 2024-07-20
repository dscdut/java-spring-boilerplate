package com.gdsc.boilerplate.springboot.service;

import com.gdsc.boilerplate.springboot.dto.request.CreateOrderRequest;
import com.gdsc.boilerplate.springboot.dto.response.CreateOrderResponse;
import com.gdsc.boilerplate.springboot.dto.response.OrderInfoResponse;
import com.gdsc.boilerplate.springboot.dto.response.PaymentStatusResponse;
import com.gdsc.boilerplate.springboot.exceptions.NotFoundException;
import com.gdsc.boilerplate.springboot.exceptions.OrderIdNotExistsException;
import com.gdsc.boilerplate.springboot.maper.order.OrderMapper;
import com.gdsc.boilerplate.springboot.model.Order;
import com.gdsc.boilerplate.springboot.model.PaymentMethod;
import com.gdsc.boilerplate.springboot.model.User;
import com.gdsc.boilerplate.springboot.model.enums.PaymentStatus;
import com.gdsc.boilerplate.springboot.payment.momo.config.Environment;
import com.gdsc.boilerplate.springboot.payment.momo.enums.RequestType;
import com.gdsc.boilerplate.springboot.payment.momo.models.PaymentResponse;
import com.gdsc.boilerplate.springboot.payment.momo.models.QueryStatusTransactionResponse;
import com.gdsc.boilerplate.springboot.payment.momo.processor.CreateOrderMoMo;
import com.gdsc.boilerplate.springboot.payment.momo.processor.QueryTransactionStatus;
import com.gdsc.boilerplate.springboot.repository.OrderRepository;
import com.gdsc.boilerplate.springboot.repository.PaymentMethodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;

  private final PaymentMethodRepository paymentMethodRepository;

  private final UserService userService;

  @Override
  @Transactional
  public CreateOrderResponse createOrder(Long userId, CreateOrderRequest createOrderRequest)
      throws Exception {
    Environment environment = Environment.selectEnv("dev");
    String requestId = String.valueOf(System.currentTimeMillis());
    String orderId = String.valueOf(System.currentTimeMillis());

    PaymentResponse paymentResponse =
        CreateOrderMoMo.process(
            environment,
            orderId,
            requestId,
            Long.toString(createOrderRequest.getAmount()),
            environment.getPartnerInfo().getOrderInfo(),
            environment.getPartnerInfo().getReturnUrl(),
            environment.getPartnerInfo().getNotifyUrl(),
            "",
            RequestType.CAPTURE_WALLET,
            Boolean.TRUE);

    Order order =
        this.save(
            createOrderRequest, paymentResponse, userId, createOrderRequest.getPaymentMethodId());

    return OrderMapper.INSTANCE.toCreateOrderResponse(order, paymentResponse.getPayUrl());
  }

  @Override
  public OrderInfoResponse getOrderById(Long orderId, Long userId) {
    Order order =
        orderRepository.findById(orderId).orElseThrow(OrderIdNotExistsException::new);

    return OrderMapper.INSTANCE.toOrderInfoResponse(order);
  }

  @Override
  public PaymentStatusResponse updateStatusOrder(Long orderId, Long userId) throws Exception {
    Order order =
        orderRepository
            .findById(orderId)
            .orElseThrow((OrderIdNotExistsException::new));

    Environment environment = Environment.selectEnv("dev");
    String requestId = String.valueOf(System.currentTimeMillis());

    QueryStatusTransactionResponse queryStatusTransactionResponse =
        QueryTransactionStatus.process(environment, order.getPaymentOrderId(), requestId);

    if (queryStatusTransactionResponse.getResultCode() == 0) {
      order.setPaymentStatus(PaymentStatus.PAID);
    }
    return OrderMapper.INSTANCE.toPaymentStatusResponse(orderRepository.save(order));
  }

  private Order save(
      CreateOrderRequest createOrderRequest,
      PaymentResponse paymentResponse,
      Long userId,
      Long paymentMethodId) {
    User user = userService.findById(userId);

    PaymentMethod paymentMethod =
        paymentMethodRepository
            .findById(paymentMethodId)
            .orElseThrow(() -> new NotFoundException("Payment method not found"));

    Order order = new Order();

    order.setUser(user);
    order.setPaymentMethod(paymentMethod);
    order.setCurrency(createOrderRequest.getCurrency());
    order.setCustomerName(createOrderRequest.getCustomerName());
    order.setCustomerPhone(createOrderRequest.getCustomerPhone());
    order.setAmount(createOrderRequest.getAmount());
    order.setPaymentOrderId(paymentResponse.getOrderId());
    order.setPaymentStatus(PaymentStatus.UNPAID);

    return orderRepository.save(order);
  }
}
