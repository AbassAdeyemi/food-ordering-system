package com.hayba.restaurant.service.messaging.mapper;


import com.hayba.domain.valueobject.ProductId;
import com.hayba.domain.valueobject.RestaurantOrderStatus;
import com.hayba.kafka.order.avro.model.OrderApprovalStatus;
import com.hayba.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.hayba.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.hayba.rabbitmq.order.model.RestaurantApprovalRequestModel;
import com.hayba.rabbitmq.order.model.RestaurantApprovalResponseModel;
import com.hayba.restaurant.service.domain.dto.RestaurantApprovalRequest;
import com.hayba.restaurant.service.domain.entity.Product;
import com.hayba.restaurant.service.domain.outbox.model.OrderEventPayload;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RestaurantMessagingDataMapper {

    public RestaurantApprovalRequest
    restaurantApprovalRequestAvroModelToRestaurantApproval(RestaurantApprovalRequestAvroModel
                                                                   restaurantApprovalRequestAvroModel) {
        return RestaurantApprovalRequest.builder()
                .id(restaurantApprovalRequestAvroModel.getId())
                .sagaId(restaurantApprovalRequestAvroModel.getSagaId())
                .restaurantId(restaurantApprovalRequestAvroModel.getRestaurantId())
                .orderId(restaurantApprovalRequestAvroModel.getOrderId())
                .restaurantOrderStatus(RestaurantOrderStatus.valueOf(restaurantApprovalRequestAvroModel
                        .getRestaurantOrderStatus().name()))
                .products(restaurantApprovalRequestAvroModel.getProducts()
                        .stream().map(avroModel ->
                                Product.builder()
                                        .productId(new ProductId(UUID.fromString(avroModel.getId())))
                                        .quantity(avroModel.getQuantity())
                                        .build())
                        .collect(Collectors.toList()))
                .price(restaurantApprovalRequestAvroModel.getPrice())
                .createdAt(restaurantApprovalRequestAvroModel.getCreatedAt())
                .build();
    }

    public RestaurantApprovalResponseAvroModel
    orderEventPayloadToRestaurantApprovalResponseAvroModel(String sagaId, OrderEventPayload orderEventPayload) {
        return RestaurantApprovalResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId(sagaId)
                .setOrderId(orderEventPayload.getOrderId())
                .setRestaurantId(orderEventPayload.getRestaurantId())
                .setCreatedAt(orderEventPayload.getCreatedAt().toInstant())
                .setOrderApprovalStatus(OrderApprovalStatus.valueOf(orderEventPayload.getOrderApprovalStatus()))
                .setFailureMessages(orderEventPayload.getFailureMessages())
                .build();
    }

    // rabbit-mq

    public RestaurantApprovalRequest
    restaurantApprovalRequestModelToRestaurantApproval(RestaurantApprovalRequestModel
                                                                   restaurantApprovalRequestModel) {
        return RestaurantApprovalRequest.builder()
                .id(restaurantApprovalRequestModel.getId())
                .sagaId(restaurantApprovalRequestModel.getSagaId())
                .restaurantId(restaurantApprovalRequestModel.getRestaurantId())
                .orderId(restaurantApprovalRequestModel.getOrderId())
                .restaurantOrderStatus(RestaurantOrderStatus.valueOf(restaurantApprovalRequestModel
                        .getRestaurantOrderStatus().name()))
                .products(restaurantApprovalRequestModel.getProducts()
                        .stream().map(avroModel ->
                                Product.builder()
                                        .productId(new ProductId(UUID.fromString(avroModel.getId())))
                                        .quantity(avroModel.getQuantity())
                                        .build())
                        .collect(Collectors.toList()))
                .price(restaurantApprovalRequestModel.getPrice())
                .createdAt(restaurantApprovalRequestModel.getCreatedAt())
                .build();
    }

    public RestaurantApprovalResponseModel
    orderEventPayloadToRestaurantApprovalResponseModel(String sagaId, OrderEventPayload orderEventPayload) {
        return RestaurantApprovalResponseModel.builder()
                .id(UUID.randomUUID().toString())
                .sagaId(sagaId)
                .orderId(orderEventPayload.getOrderId())
                .restaurantId(orderEventPayload.getRestaurantId())
                .createdAt(orderEventPayload.getCreatedAt().toInstant())
                .orderApprovalStatus(com.hayba.rabbitmq.order.model.OrderApprovalStatus.valueOf(orderEventPayload.getOrderApprovalStatus()))
                .failureMessages(orderEventPayload.getFailureMessages())
                .build();
    }
}
