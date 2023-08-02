package com.hayba.order.service.domain.event;

import com.hayba.domain.event.publisher.DomainEventPublisher;
import com.hayba.order.service.domain.entity.Order;

import java.time.ZonedDateTime;

public class OrderCancelledEvent extends OrderEvent {

    DomainEventPublisher<OrderCancelledEvent> orderCancelledEventDomainEventPublisher;
    public OrderCancelledEvent(Order order, ZonedDateTime createdAt,
                               DomainEventPublisher<OrderCancelledEvent> orderCancelledEventDomainEventPublisher) {
        super(order, createdAt);
        this.orderCancelledEventDomainEventPublisher = orderCancelledEventDomainEventPublisher;
    }

    @Override
    public void fire() {
        orderCancelledEventDomainEventPublisher.publish(this);
    }
}
