package com.hayba.order.service.messaging.listener.rabbitmq;

import com.hayba.order.service.domain.config.OrderServiceConfigData;
import com.hayba.order.service.domain.ports.input.message.listener.customer.CustomerMessageListener;
import com.hayba.order.service.messaging.mapper.OrderMessagingDataMapper;
import com.hayba.rabbitmq.consumer.RabbitMQConsumer;
import com.hayba.rabbitmq.order.model.CustomerModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "order-service", name = "messaging-platform", havingValue = "rabbit-mq")
public class CustomerRabbitMQListener implements RabbitMQConsumer<CustomerModel> {

    private final OrderServiceConfigData orderServiceConfigData;
    private final CustomerMessageListener customerMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;

    public CustomerRabbitMQListener(OrderServiceConfigData orderServiceConfigData,
                                    CustomerMessageListener customerMessageListener,
                                    OrderMessagingDataMapper orderMessagingDataMapper) {
        this.orderServiceConfigData = orderServiceConfigData;
        this.customerMessageListener = customerMessageListener;
        this.orderMessagingDataMapper = orderMessagingDataMapper;
    }

    @Override
    @RabbitListener(queues = {"#{orderServiceConfigData.customerTopicName}"})
    public void receive(CustomerModel customerModel) {
        log.info("Consumed message: {}", customerModel);
        customerMessageListener.customerCreated(orderMessagingDataMapper
                .customerRabbitModelToCustomerModel(customerModel));
    }
}
