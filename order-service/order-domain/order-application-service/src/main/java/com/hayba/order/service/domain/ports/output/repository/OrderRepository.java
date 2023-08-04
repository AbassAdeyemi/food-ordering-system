package com.hayba.order.service.domain.ports.output.repository;

import com.hayba.domain.valueobject.OrderId;
import com.hayba.order.service.domain.entity.Order;
import com.hayba.order.service.domain.valueobject.TrackingId;

import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);

    Optional<Order> findByTrackingId(TrackingId trackingId);

    Optional<Order> findById(OrderId orderId);
}
