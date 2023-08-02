package com.hayba.payment.service.domain.mapper;

import com.hayba.domain.valueobject.CustomerId;
import com.hayba.domain.valueobject.Money;
import com.hayba.domain.valueobject.OrderId;
import com.hayba.payment.service.domain.dto.PaymentRequest;
import com.hayba.payment.service.domain.entity.Payment;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PaymentDataMapper {

    public Payment paymentRequestModelToPayment(PaymentRequest paymentRequest) {
        return Payment.builder()
                .orderId(new OrderId(UUID.fromString(paymentRequest.getOrderId())))
                .customerId(new CustomerId(UUID.fromString(paymentRequest.getCustomerId())))
                .price(new Money(paymentRequest.getPrice()))
                .build();
    }
}
