package com.hayba.rabbitmq.producer.service;

import java.io.Serializable;

public interface RabbitMQProducer<V> {

    void send(String routingKey, V message, String exchangeName);

}
