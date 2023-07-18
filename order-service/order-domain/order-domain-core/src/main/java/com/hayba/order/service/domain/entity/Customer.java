package com.hayba.order.service.domain.entity;

import com.hayba.domain.entity.AggregateRoot;
import com.hayba.domain.valueobject.CustomerId;

public class Customer extends AggregateRoot<CustomerId> {
    public Customer() {}
    public Customer(CustomerId customerId) {
        super.setId(customerId);
    }
}
