package com.hayba.payment.service.domain.ports.output.message.publisher;

import com.hayba.outbox.OutboxStatus;
import com.hayba.payment.service.domain.outbox.model.OrderOutboxMessage;

import java.util.function.BiConsumer;

public interface PaymentResponseMessagePublisher {
    void publish(OrderOutboxMessage orderOutboxMessage,
                 BiConsumer<OrderOutboxMessage, OutboxStatus> outboxCallback);
}
