package com.gdsc.boilerplate.springboot.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateOrderRequest {

  @NotBlank(message = "customer_name_not_blank")
  private String customerName;

  @NotBlank(message = "customer_phone_not_blank")
  private String customerPhone;

  @NotNull(message = "amount_not_null")
  private Long amount;

  @NotBlank(message = "currency_not_blank")
  private String currency;

  @NotNull(message = "payment_method_id_not_null")
  private Long paymentMethodId;
}
