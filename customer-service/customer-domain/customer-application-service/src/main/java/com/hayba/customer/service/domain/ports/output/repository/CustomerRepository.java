package com.hayba.customer.service.domain.ports.output.repository;

import com.hayba.customer.service.domain.entity.Customer;

public interface CustomerRepository {

    Customer createCustomer(Customer customer);
}
