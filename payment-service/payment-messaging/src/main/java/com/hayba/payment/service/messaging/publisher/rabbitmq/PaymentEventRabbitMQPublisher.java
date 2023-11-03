package com.hayba.payment.service.messaging.publisher.rabbitmq;

import com.hayba.kafka.producer.KafkaMessageHelper;
import com.hayba.outbox.OutboxStatus;
import com.hayba.payment.service.domain.config.PaymentServiceConfigData;
import com.hayba.payment.service.domain.outbox.model.OrderEventPayload;
import com.hayba.payment.service.domain.outbox.model.OrderOutboxMessage;
import com.hayba.payment.service.domain.ports.output.message.publisher.PaymentResponseMessagePublisher;
import com.hayba.payment.service.messaging.mapper.PaymentMessagingDataMapper;
import com.hayba.rabbitmq.order.model.PaymentResponseModel;
import com.hayba.rabbitmq.producer.service.RabbitMQProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

import static com.hayba.domain.DomainConstants.ROUTING_KEY_SUFFIX;

@Configuration
@Component
@Slf4j
@ConditionalOnProperty(prefix = "payment-service", name = "messaging-platform", havingValue = "rabbit-mq")
public class PaymentEventRabbitMQPublisher implements PaymentResponseMessagePublisher {

    private final PaymentMessagingDataMapper paymentMessagingDataMapper;
    private final RabbitMQProducer<PaymentResponseModel> rabbitMQProducer;
    private final PaymentServiceConfigData paymentServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;

    public PaymentEventRabbitMQPublisher(PaymentMessagingDataMapper paymentMessagingDataMapper,
                                         RabbitMQProducer<PaymentResponseModel> rabbitMQProducer,
                                         PaymentServiceConfigData paymentServiceConfigData,
                                         KafkaMessageHelper kafkaMessageHelper) {
        this.paymentMessagingDataMapper = paymentMessagingDataMapper;
        this.rabbitMQProducer = rabbitMQProducer;
        this.paymentServiceConfigData = paymentServiceConfigData;
        this.kafkaMessageHelper = kafkaMessageHelper;
    }

    @Override
    public void publish(OrderOutboxMessage orderOutboxMessage, BiConsumer<OrderOutboxMessage, OutboxStatus> outboxCallback) {
        OrderEventPayload orderEventPayload =
                kafkaMessageHelper.getOrderEventPayload(orderOutboxMessage.getPayload(), OrderEventPayload.class);

        String sagaId = orderOutboxMessage.getSagaId().toString();

        log.info("Received OrderOutboxMessage for order id: {} and saga id: {}",
                orderEventPayload.getOrderId(),
                sagaId);

        PaymentResponseModel paymentResponseModel = paymentMessagingDataMapper
                .orderEventPayloadToPaymentResponseModel(sagaId, orderEventPayload);

        log.info("payment response: {}");

        try {
            rabbitMQProducer.send(paymentServiceConfigData.getPaymentResponseTopicName() + ROUTING_KEY_SUFFIX,
                    paymentResponseModel, paymentServiceConfigData.getExchangeName());
            log.info("PaymentResponseAvroModel sent to rabbit-mq for order id: {} and saga id: {}",
                    paymentResponseModel.getOrderId(), sagaId);
            outboxCallback.accept(orderOutboxMessage, OutboxStatus.COMPLETED);
        } catch (Exception e) {
            log.error("Error while sending PaymentRequestAvroModel message" +
                            " to rabbit-mq with order id: {} and saga id: {}, error: {}",
                    orderEventPayload.getOrderId(), sagaId, e.getMessage());
            outboxCallback.accept(orderOutboxMessage, OutboxStatus.FAILED);
        }
    }
}
