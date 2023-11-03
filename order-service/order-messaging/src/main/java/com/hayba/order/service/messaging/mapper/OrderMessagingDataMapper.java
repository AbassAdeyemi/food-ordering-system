package com.hayba.order.service.messaging.mapper;

import com.hayba.domain.valueobject.OrderApprovalStatus;
import com.hayba.domain.valueobject.PaymentStatus;
import com.hayba.kafka.order.avro.model.*;
import com.hayba.order.service.domain.dto.message.CustomerModel;
import com.hayba.order.service.domain.dto.message.PaymentResponse;
import com.hayba.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.hayba.order.service.domain.entity.Order;
import com.hayba.order.service.domain.event.OrderCancelledEvent;
import com.hayba.order.service.domain.event.OrderCreatedEvent;
import com.hayba.order.service.domain.event.OrderPaidEvent;
import com.hayba.order.service.domain.outbox.model.approval.OrderApprovalEventPayload;
import com.hayba.order.service.domain.outbox.model.approval.OrderApprovalEventProduct;
import com.hayba.order.service.domain.outbox.model.payment.OrderPaymentEventPayload;
import com.hayba.rabbitmq.order.model.PaymentRequestModel;
import com.hayba.rabbitmq.order.model.PaymentResponseModel;
import com.hayba.rabbitmq.order.model.RestaurantApprovalRequestModel;
import com.hayba.rabbitmq.order.model.RestaurantApprovalResponseModel;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderMessagingDataMapper {

    public PaymentRequestAvroModel orderCreatedEventToPaymentRequestAvroModel(OrderCreatedEvent orderCreatedEvent) {
        Order order = orderCreatedEvent.getOrder();
        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setCustomerId(order.getCustomerId().getValue().toString())
                .setOrderId(order.getId().getValue().toString())
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(orderCreatedEvent.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.PENDING)
                .build();
    }

    public PaymentRequestAvroModel orderCancelledEventToPaymentRequestAvroModel(OrderCancelledEvent orderCancelledEvent) {
        Order order = orderCancelledEvent.getOrder();
        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setCustomerId(order.getCustomerId().getValue().toString())
                .setOrderId(order.getId().getValue().toString())
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(orderCancelledEvent.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.CANCELLED)
                .build();
    }

    public RestaurantApprovalRequestAvroModel
    orderPaidEventToRestaurantApprovalRequestModel(OrderPaidEvent orderPaidEvent) {
        Order order = orderPaidEvent.getOrder();
        return RestaurantApprovalRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setOrderId(order.getId().getValue().toString())
                .setRestaurantId(order.getRestaurantId().getValue().toString())
                .setProducts(order.getItems().stream().map(orderItem -> Product.newBuilder()
                        .setId(orderItem.getProduct().getId().getValue().toString())
                        .setQuantity(orderItem.getQuantity())
                        .build()).collect(Collectors.toList()))
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(orderPaidEvent.getCreatedAt().toInstant())
                .setRestaurantOrderStatus(RestaurantOrderStatus.PAID)
                .build();
    }

    public PaymentResponse paymentResponseAvroModelToPaymentResponse(PaymentResponseAvroModel paymentResponseAvroModel) {
        return  PaymentResponse.builder()
                .id(paymentResponseAvroModel.getId())
                .sagaId(paymentResponseAvroModel.getSagaId())
                .paymentId(paymentResponseAvroModel.getPaymentId())
                .customerId(paymentResponseAvroModel.getCustomerId())
                .orderId(paymentResponseAvroModel.getOrderId())
                .price(paymentResponseAvroModel.getPrice())
                .createdAt(paymentResponseAvroModel.getCreatedAt())
                .paymentStatus(PaymentStatus.valueOf(paymentResponseAvroModel.getPaymentStatus().name()))
                .failureMessages(paymentResponseAvroModel.getFailureMessages())
                .build();
    }

    public RestaurantApprovalResponse approvalResponseAvroModelToApprovalResponse(RestaurantApprovalResponseAvroModel approvalResponseAvroModel) {
        return RestaurantApprovalResponse.builder()
                .id(approvalResponseAvroModel.getId())
                .sagaId(approvalResponseAvroModel.getSagaId())
                .restaurantId(approvalResponseAvroModel.getRestaurantId())
                .orderId(approvalResponseAvroModel.getOrderId())
                .createdAt(approvalResponseAvroModel.getCreatedAt())
                .orderApprovalStatus(OrderApprovalStatus.valueOf(approvalResponseAvroModel.getOrderApprovalStatus().name()))
                .failureMessages(approvalResponseAvroModel.getFailureMessages())
                .build();
    }

    public PaymentRequestAvroModel orderPaymentEventToPaymentRequestAvroModel(String sagaId, OrderPaymentEventPayload
            orderPaymentEventPayload) {
        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId(sagaId)
                .setCustomerId(orderPaymentEventPayload.getCustomerId())
                .setOrderId(orderPaymentEventPayload.getOrderId())
                .setPrice(orderPaymentEventPayload.getPrice())
                .setCreatedAt(orderPaymentEventPayload.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.valueOf(orderPaymentEventPayload.getPaymentOrderStatus()))
                .build();
    }

    public RestaurantApprovalRequestAvroModel
    orderApprovalEventToRestaurantApprovalRequestAvroModel(String sagaId, OrderApprovalEventPayload
            orderApprovalEventPayload) {
        return RestaurantApprovalRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId(sagaId)
                .setOrderId(orderApprovalEventPayload.getOrderId())
                .setRestaurantId(orderApprovalEventPayload.getRestaurantId())
                .setRestaurantOrderStatus(RestaurantOrderStatus
                        .valueOf(orderApprovalEventPayload.getRestaurantOrderStatus()))
                .setProducts(orderApprovalEventPayload.getProducts().stream().map(orderApprovalEventProduct ->
                        com.hayba.kafka.order.avro.model.Product.newBuilder()
                                .setId(orderApprovalEventProduct.getId())
                                .setQuantity(orderApprovalEventProduct.getQuantity())
                                .build()).collect(Collectors.toList()))
                .setPrice(orderApprovalEventPayload.getPrice())
                .setCreatedAt(orderApprovalEventPayload.getCreatedAt().toInstant())
                .build();
    }

    public CustomerModel customerAvroModelToCustomerModel(CustomerAvroModel customerAvroModel) {
        return CustomerModel.builder()
                .id(customerAvroModel.getId())
                .username(customerAvroModel.getUsername())
                .firstName(customerAvroModel.getFirstName())
                .lastName(customerAvroModel.getLastName())
                .build();
    }

    // rabbit-mq
    public CustomerModel customerRabbitModelToCustomerModel(com.hayba.rabbitmq.order.model.CustomerModel customerModel) {
        return CustomerModel.builder()
                .id(customerModel.getId())
                .username(customerModel.getUsername())
                .firstName(customerModel.getFirstName())
                .lastName(customerModel.getLastName())
                .build();
    }

    public PaymentRequestModel orderPaymentEventToPaymentRequestModel(String sagaId, OrderPaymentEventPayload
            orderPaymentEventPayload) {
        return PaymentRequestModel.builder()
                .id(UUID.randomUUID().toString())
                .sagaId(sagaId)
                .customerId(orderPaymentEventPayload.getCustomerId())
                .orderId(orderPaymentEventPayload.getOrderId())
                .price(orderPaymentEventPayload.getPrice())
                .createdAt(orderPaymentEventPayload.getCreatedAt().toInstant())
                .paymentOrderStatus(com.hayba.rabbitmq.order.model.PaymentOrderStatus.valueOf(orderPaymentEventPayload.getPaymentOrderStatus()))
                .build();
    }

    public RestaurantApprovalRequestModel
    orderApprovalEventToRestaurantApprovalRequestModel(String sagaId, OrderApprovalEventPayload
            orderApprovalEventPayload) {
        return RestaurantApprovalRequestModel.builder()
                .id(UUID.randomUUID().toString())
                .sagaId(sagaId)
                .orderId(orderApprovalEventPayload.getOrderId())
                .restaurantId(orderApprovalEventPayload.getRestaurantId())
                .restaurantOrderStatus(com.hayba.rabbitmq.order.model.RestaurantOrderStatus
                        .valueOf(orderApprovalEventPayload.getRestaurantOrderStatus()))
                .products(orderApprovalEventPayload.getProducts().stream().map(orderApprovalEventProduct ->
                         com.hayba.rabbitmq.order.model.Product.builder()
                                .id(orderApprovalEventProduct.getId())
                                .quantity(orderApprovalEventProduct.getQuantity())
                                .build()).collect(Collectors.toList()))
                .price(orderApprovalEventPayload.getPrice())
                .createdAt(orderApprovalEventPayload.getCreatedAt().toInstant())
                .build();
    }

    public PaymentResponse paymentResponseModelToPaymentResponse(PaymentResponseModel paymentResponseModel) {
        return  PaymentResponse.builder()
                .id(paymentResponseModel.getId())
                .sagaId(paymentResponseModel.getSagaId())
                .paymentId(paymentResponseModel.getPaymentId())
                .customerId(paymentResponseModel.getCustomerId())
                .orderId(paymentResponseModel.getOrderId())
                .price(paymentResponseModel.getPrice())
                .createdAt(paymentResponseModel.getCreatedAt())
                .paymentStatus(PaymentStatus.valueOf(paymentResponseModel.getPaymentStatus().name()))
                .failureMessages(paymentResponseModel.getFailureMessages())
                .build();
    }

    public RestaurantApprovalResponse approvalResponseModelToApprovalResponse(RestaurantApprovalResponseModel approvalResponseModel) {
        return RestaurantApprovalResponse.builder()
                .id(approvalResponseModel.getId())
                .sagaId(approvalResponseModel.getSagaId())
                .restaurantId(approvalResponseModel.getRestaurantId())
                .orderId(approvalResponseModel.getOrderId())
                .createdAt(approvalResponseModel.getCreatedAt())
                .orderApprovalStatus(OrderApprovalStatus.valueOf(approvalResponseModel.getOrderApprovalStatus().name()))
                .failureMessages(approvalResponseModel.getFailureMessages())
                .build();
    }

}
