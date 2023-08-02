package com.hayba.payment.service.domain.ports.output.message.publisher;

import com.hayba.domain.event.publisher.DomainEventPublisher;
import com.hayba.payment.service.domain.event.PaymentCancelledEvent;

public interface PaymentCancelledMessagePublisher extends DomainEventPublisher<PaymentCancelledEvent> {
}
