package com.hayba.order.service.messaging.publisher.rabbitmq;

import com.hayba.kafka.producer.KafkaMessageHelper;
import com.hayba.order.service.domain.config.OrderServiceConfigData;
import com.hayba.order.service.domain.outbox.model.payment.OrderPaymentEventPayload;
import com.hayba.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.hayba.order.service.domain.ports.output.message.publisher.payment.PaymentRequestMessagePublisher;
import com.hayba.order.service.messaging.mapper.OrderMessagingDataMapper;
import com.hayba.outbox.OutboxStatus;
import com.hayba.rabbitmq.order.model.PaymentRequestModel;
import com.hayba.rabbitmq.producer.service.RabbitMQProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

import static com.hayba.domain.DomainConstants.ROUTING_KEY_SUFFIX;

@Component
@Slf4j
@ConditionalOnProperty(prefix = "order-service", name = "messaging-platform", havingValue = "rabbit-mq")
public class OrderPaymentEventRabbitMQPublisher implements PaymentRequestMessagePublisher {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final RabbitMQProducer<PaymentRequestModel> rabbitMQProducer;
    private final OrderServiceConfigData orderServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;

    public OrderPaymentEventRabbitMQPublisher(OrderMessagingDataMapper orderMessagingDataMapper,
                                           RabbitMQProducer<PaymentRequestModel> rabbitMQProducer,
                                           OrderServiceConfigData orderServiceConfigData,
                                           KafkaMessageHelper kafkaMessageHelper) {
        this.orderMessagingDataMapper = orderMessagingDataMapper;
        this.rabbitMQProducer = rabbitMQProducer;
        this.orderServiceConfigData = orderServiceConfigData;
        this.kafkaMessageHelper = kafkaMessageHelper;
    }

    @Override
    public void publish(OrderPaymentOutboxMessage orderPaymentOutboxMessage,
                        BiConsumer<OrderPaymentOutboxMessage, OutboxStatus> outboxCallback) {

        OrderPaymentEventPayload orderPaymentEventPayload =
                kafkaMessageHelper.getOrderEventPayload(orderPaymentOutboxMessage.getPayload(),
                        OrderPaymentEventPayload.class);

        String sagaId = orderPaymentOutboxMessage.getSagaId().toString();

        log.info("Received OrderPaymentOutboxMessage for order id: {} and saga id: {}",
                orderPaymentEventPayload.getOrderId(),
                sagaId);

        PaymentRequestModel paymentRequestModel = orderMessagingDataMapper
                .orderPaymentEventToPaymentRequestModel(sagaId, orderPaymentEventPayload);

        try {
            rabbitMQProducer.send(orderServiceConfigData.getPaymentRequestTopicName() + ROUTING_KEY_SUFFIX,
                    paymentRequestModel, orderServiceConfigData.getExchangeName());

            log.info("OrderPaymentEventPayload sent to rabbit-mq for order id: {} and saga id: {}",
                    orderPaymentEventPayload.getOrderId(), sagaId);

            outboxCallback.accept(orderPaymentOutboxMessage, OutboxStatus.COMPLETED);
        }
        catch (Exception e) {
            log.error("Error while sending OrderPaymentEventPayload" +
                            " to kafka with order id: {} and saga id: {}, error: {}",
                    orderPaymentEventPayload.getOrderId(), sagaId, e.getMessage());
            outboxCallback.accept(orderPaymentOutboxMessage, OutboxStatus.FAILED);
        }
    }
}
