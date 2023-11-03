package com.hayba.restaurant.service.messaging.publisher.rabbitmq;

import com.hayba.restaurant.service.domain.config.RestaurantServiceConfigData;
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
@ConditionalOnProperty(prefix = "restaurant-service", name = "messaging-platform", havingValue = "rabbit-mq")
public class QueueConfig {

    private final RestaurantServiceConfigData restaurantServiceConfigData;

    public QueueConfig(RestaurantServiceConfigData restaurantServiceConfigData) {
        this.restaurantServiceConfigData = restaurantServiceConfigData;
    }

    @Bean
    public Declarables topicBindings() {
        Queue restaurantApprovalRequestQueue = new Queue(restaurantServiceConfigData.getRestaurantApprovalRequestTopicName());
        Queue restaurantApprovalResponseQueue = new Queue(restaurantServiceConfigData.getRestaurantApprovalResponseTopicName());

        TopicExchange orderProcessingExchange = new TopicExchange(restaurantServiceConfigData.getExchangeName());

        return new Declarables(
                restaurantApprovalRequestQueue,
                restaurantApprovalResponseQueue,
                orderProcessingExchange,
                BindingBuilder
                        .bind(restaurantApprovalRequestQueue)
                        .to(orderProcessingExchange)
                        .with(restaurantServiceConfigData.getRestaurantApprovalRequestTopicName() + ROUTING_KEY_SUFFIX),
                BindingBuilder
                        .bind(restaurantApprovalResponseQueue)
                        .to(orderProcessingExchange)
                        .with(restaurantServiceConfigData.getRestaurantApprovalResponseTopicName() + ROUTING_KEY_SUFFIX)
        );
    }
}
