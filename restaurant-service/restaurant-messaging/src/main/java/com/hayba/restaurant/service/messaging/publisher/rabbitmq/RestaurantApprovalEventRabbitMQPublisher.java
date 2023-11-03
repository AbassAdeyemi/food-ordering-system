package com.hayba.restaurant.service.messaging.publisher.rabbitmq;

import com.hayba.kafka.producer.KafkaMessageHelper;
import com.hayba.outbox.OutboxStatus;
import com.hayba.rabbitmq.order.model.RestaurantApprovalResponseModel;
import com.hayba.rabbitmq.producer.service.RabbitMQProducer;
import com.hayba.restaurant.service.domain.config.RestaurantServiceConfigData;
import com.hayba.restaurant.service.domain.outbox.model.OrderEventPayload;
import com.hayba.restaurant.service.domain.outbox.model.OrderOutboxMessage;
import com.hayba.restaurant.service.domain.ports.output.message.publisher.RestaurantApprovalResponseMessagePublisher;
import com.hayba.restaurant.service.messaging.mapper.RestaurantMessagingDataMapper;
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
@ConditionalOnProperty(prefix = "restaurant-service", name = "messaging-platform", havingValue = "rabbit-mq")
public class RestaurantApprovalEventRabbitMQPublisher implements RestaurantApprovalResponseMessagePublisher {
    private final RestaurantMessagingDataMapper restaurantMessagingDataMapper;
    private final RabbitMQProducer<RestaurantApprovalResponseModel> rabbitMQProducer;
    private final RestaurantServiceConfigData restaurantServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;

    public RestaurantApprovalEventRabbitMQPublisher(RestaurantMessagingDataMapper restaurantMessagingDataMapper,
                                                    RabbitMQProducer<RestaurantApprovalResponseModel> rabbitMQProducer,
                                                    RestaurantServiceConfigData restaurantServiceConfigData,
                                                    KafkaMessageHelper kafkaMessageHelper) {
        this.restaurantMessagingDataMapper = restaurantMessagingDataMapper;
        this.rabbitMQProducer = rabbitMQProducer;
        this.restaurantServiceConfigData = restaurantServiceConfigData;
        this.kafkaMessageHelper = kafkaMessageHelper;
    }

    @Bean
    Queue queue() {
        return new Queue(restaurantServiceConfigData.getRestaurantApprovalResponseTopicName());
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(restaurantServiceConfigData.getExchangeName());
    }

    @Bean
    Binding binding() {
        return BindingBuilder
                .bind(queue())
                .to(exchange())
                .with(restaurantServiceConfigData.getRestaurantApprovalRequestTopicName() + ROUTING_KEY_SUFFIX);
    }

    @Override
    public void publish(OrderOutboxMessage orderOutboxMessage, BiConsumer<OrderOutboxMessage, OutboxStatus> outboxCallback) {
        OrderEventPayload orderEventPayload =
                kafkaMessageHelper.getOrderEventPayload(orderOutboxMessage.getPayload(),
                        OrderEventPayload.class);

        String sagaId = orderOutboxMessage.getSagaId().toString();

        log.info("Received OrderOutboxMessage for order id: {} and saga id: {}",
                orderEventPayload.getOrderId(),
                sagaId);

        RestaurantApprovalResponseModel restaurantApprovalResponseModel =
                restaurantMessagingDataMapper.orderEventPayloadToRestaurantApprovalResponseModel(sagaId, orderEventPayload);

        try {
            rabbitMQProducer.send(restaurantServiceConfigData.getRestaurantApprovalResponseTopicName() + ROUTING_KEY_SUFFIX,
                    restaurantApprovalResponseModel, restaurantServiceConfigData.getExchangeName());
            log.info("RestaurantApprovalResponseAvroModel sent to rabbit-mq for order id: {} and saga id: {}",
                    restaurantApprovalResponseModel.getOrderId(), sagaId);
            outboxCallback.accept(orderOutboxMessage, OutboxStatus.COMPLETED);
        } catch (Exception e) {
            log.error("Error while sending RestaurantApprovalResponseAvroModel message" +
                            " to rabbit-mq with order id: {} and saga id: {}, error: {}",
                    orderEventPayload.getOrderId(), sagaId, e.getMessage());
            outboxCallback.accept(orderOutboxMessage, OutboxStatus.FAILED);
        }
    }
}
