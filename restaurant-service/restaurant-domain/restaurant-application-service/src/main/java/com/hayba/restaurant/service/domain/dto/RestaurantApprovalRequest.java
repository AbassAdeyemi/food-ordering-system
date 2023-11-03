package com.hayba.restaurant.service.domain.dto;

import com.hayba.domain.valueobject.RestaurantOrderStatus;
import com.hayba.restaurant.service.domain.entity.Product;
import lombok.*;

@Builder
@ToString
@Getter
public class RestaurantApprovalRequest {
    private String id;
    private String sagaId;
    private String restaurantId;
    private String orderId;
    private RestaurantOrderStatus restaurantOrderStatus;
    private java.util.List<Product> products;
    private java.math.BigDecimal price;
    private java.time.Instant createdAt;
}
