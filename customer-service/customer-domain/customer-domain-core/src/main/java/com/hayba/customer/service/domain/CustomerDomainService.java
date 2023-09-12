package com.hayba.customer.service.domain;

import com.hayba.customer.service.domain.entity.Customer;
import com.hayba.customer.service.domain.event.CustomerCreatedEvent;

public interface CustomerDomainService {

    CustomerCreatedEvent validateAndInitiateCustomer(Customer customer);

}
