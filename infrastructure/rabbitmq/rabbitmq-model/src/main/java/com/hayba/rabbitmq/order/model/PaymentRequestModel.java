package com.hayba.rabbitmq.order.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestModel {
    private String id;
    private String sagaId;
    private String customerId;
    private String orderId;
    private BigDecimal price;
    private Instant createdAt;
    private PaymentOrderStatus paymentOrderStatus;
}
