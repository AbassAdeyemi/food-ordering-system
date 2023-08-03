package com.hayba.restaurant.service.domain.ports.output.message.publisher;

import com.hayba.domain.event.publisher.DomainEventPublisher;
import com.hayba.restaurant.service.domain.event.OrderRejectedEvent;

public interface OrderRejectedMessagePublisher extends DomainEventPublisher<OrderRejectedEvent> {
}
