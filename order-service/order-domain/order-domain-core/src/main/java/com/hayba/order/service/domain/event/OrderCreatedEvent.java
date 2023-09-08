package com.hayba.order.service.domain.event;

import com.hayba.domain.event.publisher.DomainEventPublisher;
import com.hayba.order.service.domain.entity.Order;

import java.time.ZonedDateTime;

public class OrderCreatedEvent extends OrderEvent {

    public OrderCreatedEvent(Order order,
                             ZonedDateTime createdAt) {
        super(order, createdAt);
    }
}
