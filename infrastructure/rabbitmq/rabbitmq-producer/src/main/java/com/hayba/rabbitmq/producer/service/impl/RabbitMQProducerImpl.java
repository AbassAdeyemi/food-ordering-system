package com.hayba.rabbitmq.producer.service.impl;

import com.hayba.rabbitmq.producer.service.RabbitMQProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitMQProducerImpl<V> implements RabbitMQProducer<V> {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQProducerImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void send(String routingKey, V message, String exchangeName) {
        log.info("Sending message={} with routing key={} through exchange={}", message, routingKey, exchangeName);
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
    }

}
