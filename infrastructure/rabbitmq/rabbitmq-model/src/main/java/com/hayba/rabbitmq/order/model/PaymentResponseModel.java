package com.hayba.rabbitmq.order.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseModel {

    private String id;
    private String sagaId;
    private String paymentId;
    private String customerId;
    private String orderId;
    private BigDecimal price;
    private Instant createdAt;
    private PaymentStatus paymentStatus;
    private List<String> failureMessages;
}
