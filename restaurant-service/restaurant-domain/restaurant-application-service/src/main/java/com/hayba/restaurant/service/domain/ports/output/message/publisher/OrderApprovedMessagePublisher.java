package com.hayba.restaurant.service.domain.ports.output.message.publisher;

import com.hayba.domain.event.publisher.DomainEventPublisher;
import com.hayba.restaurant.service.domain.event.OrderApprovedEvent;

public interface OrderApprovedMessagePublisher extends DomainEventPublisher<OrderApprovedEvent> {
}
