package com.hayba.order.service.messaging.publisher.rabbitmq;

import com.hayba.order.service.domain.config.OrderServiceConfigData;
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
@ConditionalOnProperty(prefix = "order-service", name = "messaging-platform", havingValue = "rabbit-mq")
public class QueueConfig {

    public QueueConfig(OrderServiceConfigData orderServiceConfigData) {
        this.orderServiceConfigData = orderServiceConfigData;
    }

    private final OrderServiceConfigData orderServiceConfigData;

    @Bean
    public Declarables topicBindings() {
        Queue paymentRequestQueue = new Queue(orderServiceConfigData.getPaymentRequestTopicName());
        Queue restaurantApprovalRequestQueue = new Queue(orderServiceConfigData.getRestaurantApprovalRequestTopicName());
        Queue paymentResponseQueue = new Queue(orderServiceConfigData.getPaymentResponseTopicName());
        Queue restaurantApprovalResponseQueue = new Queue(orderServiceConfigData.getRestaurantApprovalResponseTopicName());
        Queue customerQueue = new Queue(orderServiceConfigData.getCustomerTopicName());

        TopicExchange orderProcessingExchange = new TopicExchange(orderServiceConfigData.getExchangeName());

        return new Declarables(
                paymentRequestQueue,
                restaurantApprovalRequestQueue,
                paymentResponseQueue,
                restaurantApprovalResponseQueue,
                customerQueue,
                orderProcessingExchange,
                BindingBuilder
                        .bind(paymentRequestQueue)
                        .to(orderProcessingExchange)
                        .with(orderServiceConfigData.getPaymentRequestTopicName() + ROUTING_KEY_SUFFIX),
                BindingBuilder
                        .bind(restaurantApprovalRequestQueue)
                        .to(orderProcessingExchange)
                        .with(orderServiceConfigData.getRestaurantApprovalRequestTopicName() + ROUTING_KEY_SUFFIX),
                BindingBuilder
                        .bind(paymentResponseQueue)
                        .to(orderProcessingExchange)
                        .with(orderServiceConfigData.getPaymentResponseTopicName() + ROUTING_KEY_SUFFIX),
                BindingBuilder
                        .bind(restaurantApprovalResponseQueue)
                        .to(orderProcessingExchange)
                        .with(orderServiceConfigData.getRestaurantApprovalResponseTopicName() + ROUTING_KEY_SUFFIX),
                BindingBuilder
                        .bind(customerQueue)
                        .to(orderProcessingExchange)
                        .with(orderServiceConfigData.getCustomerTopicName() + ROUTING_KEY_SUFFIX)
        );
    }
}
