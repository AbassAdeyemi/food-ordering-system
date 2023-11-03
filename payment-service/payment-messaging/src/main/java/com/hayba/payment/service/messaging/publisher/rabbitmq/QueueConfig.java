package com.hayba.payment.service.messaging.publisher.rabbitmq;

import com.hayba.payment.service.domain.config.PaymentServiceConfigData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static com.hayba.domain.DomainConstants.ROUTING_KEY_SUFFIX;

@Component
@Slf4j
@ConditionalOnProperty(prefix = "payment-service", name = "messaging-platform", havingValue = "rabbit-mq")
public class QueueConfig {

    private final PaymentServiceConfigData paymentServiceConfigData;

    public QueueConfig(PaymentServiceConfigData paymentServiceConfigData) {
        this.paymentServiceConfigData = paymentServiceConfigData;
    }

    @Bean
    public Declarables topicBindings() {
        Queue paymentRequestQueue = new Queue(paymentServiceConfigData.getPaymentRequestTopicName());
        Queue paymentResponseQueue = new Queue(paymentServiceConfigData.getPaymentResponseTopicName());

        TopicExchange orderProcessingExchange = new TopicExchange(paymentServiceConfigData.getExchangeName());

        return new Declarables(
                paymentRequestQueue,
                paymentResponseQueue,
                orderProcessingExchange,
                BindingBuilder
                        .bind(paymentRequestQueue)
                        .to(orderProcessingExchange)
                        .with(paymentServiceConfigData.getPaymentRequestTopicName() + ROUTING_KEY_SUFFIX),
                BindingBuilder
                        .bind(paymentResponseQueue)
                        .to(orderProcessingExchange)
                        .with(paymentServiceConfigData.getPaymentResponseTopicName() + ROUTING_KEY_SUFFIX)
        );
    }
}
