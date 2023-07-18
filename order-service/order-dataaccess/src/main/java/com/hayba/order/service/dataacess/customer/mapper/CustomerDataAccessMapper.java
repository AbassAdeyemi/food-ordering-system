package com.hayba.order.service.dataacess.customer.mapper;

import com.hayba.domain.valueobject.CustomerId;
import com.hayba.order.service.dataacess.customer.entity.CustomerEntity;
import com.hayba.order.service.domain.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerDataAccessMapper {

    public Customer customerEntityToCustomer(CustomerEntity customerEntity) {
        return new Customer(new CustomerId(customerEntity.getId()));
    }
}
