package com.hayba.order.service.domain.mapper;

import com.hayba.domain.valueobject.CustomerId;
import com.hayba.domain.valueobject.Money;
import com.hayba.domain.valueobject.ProductId;
import com.hayba.domain.valueobject.RestaurantId;
import com.hayba.order.service.domain.dto.create.CreateOrderCommand;
import com.hayba.order.service.domain.dto.create.CreateOrderResponse;
import com.hayba.order.service.domain.dto.create.OrderAddress;
import com.hayba.order.service.domain.dto.track.TrackOrderResponse;
import com.hayba.order.service.domain.entity.Order;
import com.hayba.order.service.domain.entity.OrderItem;
import com.hayba.order.service.domain.entity.Product;
import com.hayba.order.service.domain.entity.Restaurant;
import com.hayba.order.service.domain.valueobject.StreetAddress;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class OrderDataMapper {

    public Restaurant createOrderCommandToRestaurant(CreateOrderCommand createOrderCommand) {
        return Restaurant.Builder.builder()
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .products(createOrderCommand.getItems().stream().map(orderItem ->
                                new Product(new ProductId(orderItem.getProductId())))
                        .toList()).build();
    }

    public Order createOrderCommandToOrder(CreateOrderCommand createOrderCommand) {
        return Order.Builder.builder()
                .customerId(new CustomerId(createOrderCommand.getCustomerId()))
                .restaurantID(new RestaurantId(createOrderCommand.getRestaurantId()))
                .deliveryAddress(orderAddressToStreetAddress(createOrderCommand.getAddress()))
                .price(new Money(createOrderCommand.getPrice()))
                .items(orderItemsToOrderItemsEntities(createOrderCommand.getItems()))
                .build();
    }

    public CreateOrderResponse orderToCreateOrderResponse(Order order, String message) {
        return CreateOrderResponse.builder()
                .orderTrackingId(order.getTrackingId().getValue())
                .orderStatus(order.getOrderStatus())
                .message(message)
                .build();
    }

    public TrackOrderResponse orderToTrackOrderResponse(Order order) {
        return TrackOrderResponse.builder()
                .orderTrackingId(order.getTrackingId().getValue())
                .orderStatus(order.getOrderStatus())
                .failureMessages(order.getFailureMessages())
                .build();
    }

    private List<OrderItem> orderItemsToOrderItemsEntities(List<com.hayba.order.service.domain.dto.create.OrderItem> orderItems) {
        return orderItems.stream().map(orderItem -> {
            return OrderItem.Builder.builder()
                    .product(new Product(new ProductId(orderItem.getProductId())))
                    .price(new Money(orderItem.getPrice()))
                    .quantity(orderItem.getQuantity())
                    .subTotal(new Money(orderItem.getSubTotal()))
                    .build();
        }).toList();
    }

    private StreetAddress orderAddressToStreetAddress(OrderAddress orderAddress) {
        return new StreetAddress(
                UUID.randomUUID(),
                orderAddress.getStreet(),
                orderAddress.getPostalCode(),
                orderAddress.getCity()
        );
    }
}
