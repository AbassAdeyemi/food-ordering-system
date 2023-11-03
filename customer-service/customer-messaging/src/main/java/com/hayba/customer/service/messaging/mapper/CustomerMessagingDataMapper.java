package com.hayba.customer.service.messaging.mapper;

import com.hayba.customer.service.domain.event.CustomerCreatedEvent;
import com.hayba.kafka.order.avro.model.CustomerAvroModel;
import com.hayba.rabbitmq.order.model.CustomerModel;
import org.springframework.stereotype.Component;

@Component
public class CustomerMessagingDataMapper {

    public CustomerAvroModel customerCreatedEventToCustomerAvroModel(CustomerCreatedEvent
                                                                               customerCreatedEvent) {
        return CustomerAvroModel.newBuilder()
                .setId(customerCreatedEvent.getCustomer().getId().getValue().toString())
                .setUsername(customerCreatedEvent.getCustomer().getUsername())
                .setFirstName(customerCreatedEvent.getCustomer().getFirstName())
                .setLastName(customerCreatedEvent.getCustomer().getLastName())
                .build();
    }


    // rabbit-mq
    public CustomerModel customerCreatedEventToCustomerModel(CustomerCreatedEvent
                                                                             customerCreatedEvent) {
        return CustomerModel.builder()
                .id(customerCreatedEvent.getCustomer().getId().getValue().toString())
                .username(customerCreatedEvent.getCustomer().getUsername())
                .firstName(customerCreatedEvent.getCustomer().getFirstName())
                .lastName(customerCreatedEvent.getCustomer().getLastName())
                .build();
    }
}
