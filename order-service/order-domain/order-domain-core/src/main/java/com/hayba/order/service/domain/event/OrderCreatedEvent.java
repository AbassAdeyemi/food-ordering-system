package com.hayba.order.service.domain.event;

import com.hayba.domain.event.publisher.DomainEventPublisher;
import com.hayba.order.service.domain.entity.Order;

import java.time.ZonedDateTime;

public class OrderCreatedEvent extends OrderEvent {

    DomainEventPublisher<OrderCreatedEvent> orderCreatedEventDomainEventPublisher;
    public OrderCreatedEvent(Order order, ZonedDateTime createdAt,
                             DomainEventPublisher<OrderCreatedEvent> orderCreatedEventDomainEventPublisher) {
        super(order, createdAt);
        this.orderCreatedEventDomainEventPublisher = orderCreatedEventDomainEventPublisher;
    }

    @Override
    public void fire() {
        orderCreatedEventDomainEventPublisher.publish(this);
    }
}
