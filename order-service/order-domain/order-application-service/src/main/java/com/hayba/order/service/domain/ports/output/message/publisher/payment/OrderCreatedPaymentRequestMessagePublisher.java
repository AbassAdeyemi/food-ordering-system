package com.hayba.order.service.domain.ports.output.message.publisher.payment;

import com.hayba.domain.event.publisher.DomainEventPublisher;
import com.hayba.order.service.domain.event.OrderCreatedEvent;

public interface OrderCreatedPaymentRequestMessagePublisher extends DomainEventPublisher<OrderCreatedEvent> {
}
