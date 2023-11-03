package com.hayba.customer.service.messaging.publisher.rabbitmq;

import com.hayba.customer.service.domain.config.CustomerServiceConfigData;
import com.hayba.customer.service.domain.event.CustomerCreatedEvent;
import com.hayba.customer.service.domain.ports.output.message.publisher.CustomerMessagePublisher;
import com.hayba.customer.service.messaging.mapper.CustomerMessagingDataMapper;
import com.hayba.domain.DomainConstants;
import com.hayba.kafka.order.avro.model.CustomerAvroModel;
import com.hayba.rabbitmq.order.model.CustomerModel;
import com.hayba.rabbitmq.producer.service.RabbitMQProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import static com.hayba.domain.DomainConstants.ROUTING_KEY_SUFFIX;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "customer-service", name = "messaging-platform", havingValue = "rabbit-mq")
public class CustomerCreatedEventRabbitMQPublisher implements CustomerMessagePublisher {

    private final RabbitMQProducer<CustomerModel> rabbitMQProducer;

    private final CustomerMessagingDataMapper customerMessagingDataMapper;

    private final CustomerServiceConfigData customerServiceConfigData;

    public CustomerCreatedEventRabbitMQPublisher(RabbitMQProducer<CustomerModel> rabbitMQProducer,
                                                 CustomerMessagingDataMapper customerMessagingDataMapper,
                                                 CustomerServiceConfigData customerServiceConfigData) {
        this.rabbitMQProducer = rabbitMQProducer;
        this.customerMessagingDataMapper = customerMessagingDataMapper;
        this.customerServiceConfigData = customerServiceConfigData;
    }

    @Bean
    public Declarables topicBindings() {
        Queue queue = new Queue(customerServiceConfigData.getCustomerTopicName());
        TopicExchange exchange = new TopicExchange(customerServiceConfigData.getExchangeName());
        return new Declarables(
                queue,
                exchange,
                BindingBuilder
                        .bind(queue)
                        .to(exchange)
                        .with(customerServiceConfigData.getCustomerTopicName() + ROUTING_KEY_SUFFIX)
        );
    }

    @Override
    public void publish(CustomerCreatedEvent customerCreatedEvent) {
        log.info("Received CustomerCreatedEvent for customer id: {}",
                customerCreatedEvent.getCustomer().getId().getValue());

        try {
            CustomerModel customerModel = customerMessagingDataMapper
                    .customerCreatedEventToCustomerModel(customerCreatedEvent);

            rabbitMQProducer.send(customerServiceConfigData.getCustomerTopicName() + ROUTING_KEY_SUFFIX,
                    customerModel, customerServiceConfigData.getExchangeName());
        } catch (Exception e) {
            log.error("Error while sending CustomerCreatedEvent to rabbitmq for customer id: {}," +
                    " error: {}", customerCreatedEvent.getCustomer().getId().getValue(), e.getMessage());
        }
    }
}
