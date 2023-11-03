package com.hayba.restaurant.service.messaging.listener.rabbitmq;

import com.hayba.rabbitmq.consumer.RabbitMQConsumer;
import com.hayba.rabbitmq.order.model.RestaurantApprovalRequestModel;
import com.hayba.restaurant.service.domain.config.RestaurantServiceConfigData;
import com.hayba.restaurant.service.domain.ports.input.message.listener.RestaurantApprovalRequestMessageListener;
import com.hayba.restaurant.service.messaging.mapper.RestaurantMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@ConditionalOnProperty(prefix = "restaurant-service", name = "messaging-platform", havingValue = "rabbit-mq")
public class RestaurantApprovalRequestRabbitMQListener implements RabbitMQConsumer<RestaurantApprovalRequestModel> {

    private final RestaurantApprovalRequestMessageListener restaurantApprovalRequestMessageListener;
    private final RestaurantMessagingDataMapper restaurantMessagingDataMapper;
    private final RestaurantServiceConfigData restaurantServiceConfigData;

    public RestaurantApprovalRequestRabbitMQListener(RestaurantApprovalRequestMessageListener restaurantApprovalRequestMessageListener,
                                                     RestaurantMessagingDataMapper restaurantMessagingDataMapper,
                                                     RestaurantServiceConfigData restaurantServiceConfigData) {
        this.restaurantApprovalRequestMessageListener = restaurantApprovalRequestMessageListener;
        this.restaurantMessagingDataMapper = restaurantMessagingDataMapper;
        this.restaurantServiceConfigData = restaurantServiceConfigData;
    }

    @Override
    @RabbitListener(queues = {"#{restaurantServiceConfigData.restaurantApprovalRequestTopicName}"})
    public void receive(RestaurantApprovalRequestModel restaurantApprovalRequestModel) {
        log.info("Processing order approval for order id: {}", restaurantApprovalRequestModel.getOrderId());
        restaurantApprovalRequestMessageListener.approveOrder(restaurantMessagingDataMapper.
                restaurantApprovalRequestModelToRestaurantApproval(restaurantApprovalRequestModel));
    }
}
