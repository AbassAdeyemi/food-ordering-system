package com.hayba.order.service.messaging.publisher.rabbitmq;

import com.hayba.kafka.producer.KafkaMessageHelper;
import com.hayba.order.service.domain.config.OrderServiceConfigData;
import com.hayba.order.service.domain.outbox.model.approval.OrderApprovalEventPayload;
import com.hayba.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.hayba.order.service.domain.ports.output.message.publisher.restaurantapproval.RestaurantApprovalRequestMessagePublisher;
import com.hayba.order.service.messaging.mapper.OrderMessagingDataMapper;
import com.hayba.outbox.OutboxStatus;
import com.hayba.rabbitmq.order.model.RestaurantApprovalRequestModel;
import com.hayba.rabbitmq.producer.service.RabbitMQProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

import static com.hayba.domain.DomainConstants.ROUTING_KEY_SUFFIX;

@Component
@Slf4j
@ConditionalOnProperty(prefix = "order-service", name = "messaging-platform", havingValue = "rabbit-mq")
public class OrderApprovalEventRabbitMQPublisher implements RestaurantApprovalRequestMessagePublisher {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final RabbitMQProducer<RestaurantApprovalRequestModel> rabbitMQProducer;
    private final OrderServiceConfigData orderServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;

    public OrderApprovalEventRabbitMQPublisher(OrderMessagingDataMapper orderMessagingDataMapper,
                                            RabbitMQProducer<RestaurantApprovalRequestModel> rabbitMQProducer,
                                            OrderServiceConfigData orderServiceConfigData,
                                            KafkaMessageHelper kafkaMessageHelper) {
        this.orderMessagingDataMapper = orderMessagingDataMapper;
        this.rabbitMQProducer = rabbitMQProducer;
        this.orderServiceConfigData = orderServiceConfigData;
        this.kafkaMessageHelper = kafkaMessageHelper;
    }

    @Override
    public void publish(OrderApprovalOutboxMessage orderApprovalOutboxMessage,
                        BiConsumer<OrderApprovalOutboxMessage, OutboxStatus> outboxCallback) {
        OrderApprovalEventPayload orderApprovalEventPayload =
                kafkaMessageHelper.getOrderEventPayload(orderApprovalOutboxMessage.getPayload(), OrderApprovalEventPayload.class);

        String sagaId = orderApprovalOutboxMessage.getSagaId().toString();

        log.info("Received OrderApprovalOutboxMessage for order id: {} and saga id: {}",
                orderApprovalEventPayload.getOrderId(),
                sagaId);

        RestaurantApprovalRequestModel restaurantApprovalRequestModel =
                orderMessagingDataMapper.orderApprovalEventToRestaurantApprovalRequestModel(sagaId, orderApprovalEventPayload);

        log.info("approval event>>>>>>>>>>: {}", orderApprovalEventPayload.getProducts());

        try {
            rabbitMQProducer.send(orderServiceConfigData.getRestaurantApprovalRequestTopicName() + ROUTING_KEY_SUFFIX,
                    restaurantApprovalRequestModel, orderServiceConfigData.getExchangeName());
            log.info("OrderApprovalEventPayload sent to rabbitmq for order id: {} and saga id: {}",
                    restaurantApprovalRequestModel.getOrderId(), sagaId);
            outboxCallback.accept(orderApprovalOutboxMessage, OutboxStatus.COMPLETED);
        } catch (Exception e) {
            log.error("Error while sending OrderApprovalEventPayload to rabbitmq for order id: {} and saga id: {}," +
                    " error: {}", orderApprovalEventPayload.getOrderId(), sagaId, e.getMessage());
            outboxCallback.accept(orderApprovalOutboxMessage, OutboxStatus.FAILED);
        }
    }
}
