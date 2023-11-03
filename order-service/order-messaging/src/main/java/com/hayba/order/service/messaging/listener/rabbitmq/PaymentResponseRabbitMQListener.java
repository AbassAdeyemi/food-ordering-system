package com.hayba.order.service.messaging.listener.rabbitmq;

import com.hayba.order.service.domain.config.OrderServiceConfigData;
import com.hayba.order.service.domain.ports.input.message.listener.payment.PaymentResponseMessageListener;
import com.hayba.order.service.messaging.mapper.OrderMessagingDataMapper;
import com.hayba.rabbitmq.consumer.RabbitMQConsumer;
import com.hayba.rabbitmq.order.model.PaymentResponseModel;
import com.hayba.rabbitmq.order.model.PaymentStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@ConditionalOnProperty(prefix = "order-service", name = "messaging-platform", havingValue = "rabbit-mq")
public class PaymentResponseRabbitMQListener implements RabbitMQConsumer<PaymentResponseModel> {

    private final PaymentResponseMessageListener paymentResponseMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final OrderServiceConfigData orderServiceConfigData;

    public PaymentResponseRabbitMQListener(PaymentResponseMessageListener paymentResponseMessageListener,
                                           OrderMessagingDataMapper orderMessagingDataMapper,
                                           OrderServiceConfigData orderServiceConfigData) {
        this.paymentResponseMessageListener = paymentResponseMessageListener;
        this.orderMessagingDataMapper = orderMessagingDataMapper;
        this.orderServiceConfigData = orderServiceConfigData;
    }

    @Override
    @RabbitListener(queues = {"#{orderServiceConfigData.getPaymentResponseTopicName}"})
    public void receive(PaymentResponseModel paymentResponseModel) {
        if(com.hayba.rabbitmq.order.model.PaymentStatus.COMPLETED == paymentResponseModel.getPaymentStatus()) {
            log.info("Processing successful payment for order id: {}", paymentResponseModel.getOrderId());
            paymentResponseMessageListener.paymentCompleted(orderMessagingDataMapper.paymentResponseModelToPaymentResponse(paymentResponseModel));
        } else if(com.hayba.rabbitmq.order.model.PaymentStatus.FAILED == paymentResponseModel.getPaymentStatus() ||
                PaymentStatus.CANCELLED == paymentResponseModel.getPaymentStatus()) {
            paymentResponseMessageListener.paymentCancelled(orderMessagingDataMapper.paymentResponseModelToPaymentResponse(paymentResponseModel));
        }
    }
}
