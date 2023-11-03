package com.hayba.rabbitmq.order.model;

import lombok.*;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class RestaurantApprovalResponseModel {
    private String id;
    private String sagaId;
    private String restaurantId;
    private String orderId;
    private java.time.Instant createdAt;
    private OrderApprovalStatus orderApprovalStatus;
    private List<String> failureMessages;
}
