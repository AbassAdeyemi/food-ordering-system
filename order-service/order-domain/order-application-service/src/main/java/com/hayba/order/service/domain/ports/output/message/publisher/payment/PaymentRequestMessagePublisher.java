package com.hayba.order.service.domain.ports.output.message.publisher.payment;

import com.hayba.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.hayba.outbox.OutboxStatus;

import java.util.function.BiConsumer;

public interface PaymentRequestMessagePublisher {

    void publish(OrderPaymentOutboxMessage orderPaymentOutboxMessage,
                 BiConsumer<OrderPaymentOutboxMessage, OutboxStatus> outboxCallback);
}
