package com.hayba.customer.service.domain.ports.output.message.publisher;

import com.hayba.customer.service.domain.event.CustomerCreatedEvent;

public interface CustomerMessagePublisher {

    void publish(CustomerCreatedEvent customerCreatedEvent);

}