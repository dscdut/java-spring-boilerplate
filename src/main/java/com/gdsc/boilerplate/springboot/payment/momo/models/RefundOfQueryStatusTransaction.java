package com.gdsc.boilerplate.springboot.payment.momo.models;

import lombok.Data;

@Data
public class RefundOfQueryStatusTransaction {
    private String orderId;
    private Long amount;
    private Integer resultCode;
    private Long transId;
    private Long createdTime;
}
