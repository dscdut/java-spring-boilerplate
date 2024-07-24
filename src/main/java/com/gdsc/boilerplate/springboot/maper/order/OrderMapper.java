package com.gdsc.boilerplate.springboot.maper.order;

import com.gdsc.boilerplate.springboot.dto.response.CreateOrderResponse;
import com.gdsc.boilerplate.springboot.dto.response.OrderInfoResponse;
import com.gdsc.boilerplate.springboot.dto.response.PaymentStatusResponse;
import com.gdsc.boilerplate.springboot.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

  OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

  @Mapping(source = "paymentUrl", target = "paymentUrl")
  @Mapping(source = "order.id", target = "orderId")
  @Mapping(source = "order.paymentStatus", target = "status")
  @Mapping(source = "order.amount", target = "amount")
  @Mapping(source = "order.paymentMethod.name", target = "paymentMethodName")
  @Mapping(source = "order.paymentOrderId", target = "paymentOrderId")
  CreateOrderResponse toCreateOrderResponse(Order order, String paymentUrl);

  @Mapping(source = "order.paymentStatus", target = "status")
  @Mapping(source = "order.amount", target = "amount")
  @Mapping(source = "order.paymentMethod.name", target = "paymentMethodName")
  @Mapping(source = "order.paymentOrderId", target = "paymentOrderId")
  @Mapping(source = "order.currency", target = "currency")
  OrderInfoResponse toOrderInfoResponse(Order order);

  @Mapping(source = "order.paymentStatus", target = "status")
  PaymentStatusResponse toPaymentStatusResponse(Order order);
}
