package com.hayba.order.service.domain.ports.output.message.publisher.payment;

import com.hayba.domain.event.publisher.DomainEventPublisher;
import com.hayba.order.service.domain.event.OrderCancelledEvent;

public interface OrderCancelledPaymentRequestMessagePublisher extends DomainEventPublisher<OrderCancelledEvent> {
}
