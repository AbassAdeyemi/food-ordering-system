package com.hayba.payment.service.messaging.mapper;

import com.hayba.domain.valueobject.PaymentOrderStatus;
import com.hayba.kafka.order.avro.model.PaymentRequestAvroModel;
import com.hayba.kafka.order.avro.model.PaymentResponseAvroModel;
import com.hayba.kafka.order.avro.model.PaymentStatus;
import com.hayba.payment.service.domain.dto.PaymentRequest;
import com.hayba.payment.service.domain.event.PaymentCancelledEvent;
import com.hayba.payment.service.domain.event.PaymentCompletedEvent;
import com.hayba.payment.service.domain.event.PaymentFailedEvent;
import com.hayba.payment.service.domain.outbox.model.OrderEventPayload;
import com.hayba.rabbitmq.order.model.PaymentRequestModel;
import com.hayba.rabbitmq.order.model.PaymentResponseModel;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PaymentMessagingDataMapper {
    public PaymentRequest paymentRequestAvroModelToPaymentRequest(PaymentRequestAvroModel paymentRequestAvroModel) {
        return PaymentRequest.builder()
                .id(paymentRequestAvroModel.getId())
                .sagaId(paymentRequestAvroModel.getSagaId())
                .customerId(paymentRequestAvroModel.getCustomerId())
                .orderId(paymentRequestAvroModel.getOrderId())
                .price(paymentRequestAvroModel.getPrice())
                .createdAt(paymentRequestAvroModel.getCreatedAt())
                .paymentOrderStatus(PaymentOrderStatus.valueOf(paymentRequestAvroModel.getPaymentOrderStatus().name()))
                .build();
    }

    public PaymentResponseAvroModel orderEventPayloadToPaymentResponseAvroModel(String sagaId,
                                                                                OrderEventPayload orderEventPayload) {
        return PaymentResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId(sagaId)
                .setPaymentId(orderEventPayload.getPaymentId())
                .setCustomerId(orderEventPayload.getCustomerId())
                .setOrderId(orderEventPayload.getOrderId())
                .setPrice(orderEventPayload.getPrice())
                .setCreatedAt(orderEventPayload.getCreatedAt().toInstant())
                .setPaymentStatus(PaymentStatus.valueOf(orderEventPayload.getPaymentStatus()))
                .setFailureMessages(orderEventPayload.getFailureMessages())
                .build();
    }

    // rabbit-mq

    public PaymentRequest paymentRequestModelToPaymentRequest(PaymentRequestModel paymentRequestModel) {
        return PaymentRequest.builder()
                .id(paymentRequestModel.getId())
                .sagaId(paymentRequestModel.getSagaId())
                .customerId(paymentRequestModel.getCustomerId())
                .orderId(paymentRequestModel.getOrderId())
                .price(paymentRequestModel.getPrice())
                .createdAt(paymentRequestModel.getCreatedAt())
                .paymentOrderStatus(PaymentOrderStatus.valueOf(paymentRequestModel.getPaymentOrderStatus().name()))
                .build();
    }

    public PaymentResponseModel orderEventPayloadToPaymentResponseModel(String sagaId,
                                                                            OrderEventPayload orderEventPayload) {
        return PaymentResponseModel.builder()
                .id(UUID.randomUUID().toString())
                .sagaId(sagaId)
                .paymentId(orderEventPayload.getPaymentId())
                .customerId(orderEventPayload.getCustomerId())
                .orderId(orderEventPayload.getOrderId())
                .price(orderEventPayload.getPrice())
                .createdAt(orderEventPayload.getCreatedAt().toInstant())
                .paymentStatus(com.hayba.rabbitmq.order.model.PaymentStatus.valueOf(orderEventPayload.getPaymentStatus()))
                .failureMessages(orderEventPayload.getFailureMessages())
                .build();
    }
}
