package com.hayba.payment.service.messaging.listener.rabbitmq;

import com.hayba.payment.service.domain.config.PaymentServiceConfigData;
import com.hayba.payment.service.domain.ports.input.message.listener.PaymentRequestMessageListener;
import com.hayba.payment.service.messaging.mapper.PaymentMessagingDataMapper;
import com.hayba.rabbitmq.consumer.RabbitMQConsumer;
import com.hayba.rabbitmq.order.model.PaymentOrderStatus;
import com.hayba.rabbitmq.order.model.PaymentRequestModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@ConditionalOnProperty(prefix = "payment-service", name = "messaging-platform", havingValue = "rabbit-mq")
public class PaymentRequestRabbitMQListener implements RabbitMQConsumer<PaymentRequestModel> {

    private final PaymentRequestMessageListener paymentRequestMessageListener;
    private final PaymentMessagingDataMapper paymentMessagingDataMapper;

    private final PaymentServiceConfigData paymentServiceConfigData;

    public PaymentRequestRabbitMQListener(PaymentRequestMessageListener paymentRequestMessageListener,
                                          PaymentMessagingDataMapper paymentMessagingDataMapper,
                                          PaymentServiceConfigData paymentServiceConfigData) {
        this.paymentRequestMessageListener = paymentRequestMessageListener;
        this.paymentMessagingDataMapper = paymentMessagingDataMapper;
        this.paymentServiceConfigData = paymentServiceConfigData;
    }

    @Override
    @RabbitListener(queues = {"#{paymentServiceConfigData.paymentRequestTopicName}"})
    public void receive(PaymentRequestModel paymentRequestModel) {
        if (com.hayba.rabbitmq.order.model.PaymentOrderStatus.PENDING == paymentRequestModel.getPaymentOrderStatus()) {
            log.info("Processing payment for order id: {}", paymentRequestModel.getOrderId());
            paymentRequestMessageListener.completePayment(paymentMessagingDataMapper
                    .paymentRequestModelToPaymentRequest(paymentRequestModel));
        } else if(PaymentOrderStatus.CANCELLED == paymentRequestModel.getPaymentOrderStatus()) {
            log.info("Cancelling payment for order id: {}", paymentRequestModel.getOrderId());
            paymentRequestMessageListener.cancelPayment(paymentMessagingDataMapper
                    .paymentRequestModelToPaymentRequest(paymentRequestModel));
        }
    }
}
