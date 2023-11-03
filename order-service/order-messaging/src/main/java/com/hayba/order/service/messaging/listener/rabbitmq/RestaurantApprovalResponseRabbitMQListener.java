package com.hayba.order.service.messaging.listener.rabbitmq;

import com.hayba.order.service.domain.config.OrderServiceConfigData;
import com.hayba.order.service.domain.ports.input.message.listener.restaurantapproval.RestaurantApprovalResponseMessageListener;
import com.hayba.order.service.messaging.mapper.OrderMessagingDataMapper;
import com.hayba.rabbitmq.consumer.RabbitMQConsumer;
import com.hayba.rabbitmq.order.model.OrderApprovalStatus;
import com.hayba.rabbitmq.order.model.RestaurantApprovalResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import static com.hayba.order.service.domain.entity.Order.FAILURE_MESSAGE_DELIMETER;

@Component
@Slf4j
@ConditionalOnProperty(prefix = "order-service", name = "messaging-platform", havingValue = "rabbit-mq")
public class RestaurantApprovalResponseRabbitMQListener implements RabbitMQConsumer<RestaurantApprovalResponseModel> {

    private final RestaurantApprovalResponseMessageListener restaurantApprovalResponseMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final OrderServiceConfigData orderServiceConfigData;

    public RestaurantApprovalResponseRabbitMQListener(RestaurantApprovalResponseMessageListener restaurantApprovalResponseMessageListener,
                                                      OrderMessagingDataMapper orderMessagingDataMapper,
                                                      OrderServiceConfigData orderServiceConfigData) {
        this.restaurantApprovalResponseMessageListener = restaurantApprovalResponseMessageListener;
        this.orderMessagingDataMapper = orderMessagingDataMapper;
        this.orderServiceConfigData = orderServiceConfigData;
    }

    @Override
    @RabbitListener(queues = {"#{orderServiceConfigData.getRestaurantApprovalResponseTopicName}"})
    public void receive(RestaurantApprovalResponseModel restaurantApprovalResponseModel) {
        if (com.hayba.rabbitmq.order.model.OrderApprovalStatus.APPROVED == restaurantApprovalResponseModel.getOrderApprovalStatus()) {
            log.info("Processing approved order for order id: {}",
                    restaurantApprovalResponseModel.getOrderId());
            restaurantApprovalResponseMessageListener.orderApproved(orderMessagingDataMapper.
                    approvalResponseModelToApprovalResponse(restaurantApprovalResponseModel));
        } else if (OrderApprovalStatus.REJECTED == restaurantApprovalResponseModel.getOrderApprovalStatus()) {
            log.info("Processing rejected order for order id: {} with failure messages: {}",
                    restaurantApprovalResponseModel.getOrderId(),
                    String.join(FAILURE_MESSAGE_DELIMETER, restaurantApprovalResponseModel.getFailureMessages()));
            restaurantApprovalResponseMessageListener.orderRejected(orderMessagingDataMapper.
                    approvalResponseModelToApprovalResponse(restaurantApprovalResponseModel));
        }
    }
}
