package com.hayba.rabbitmq.consumer;

public interface RabbitMQConsumer<V> {

    void receive(V message);
}
