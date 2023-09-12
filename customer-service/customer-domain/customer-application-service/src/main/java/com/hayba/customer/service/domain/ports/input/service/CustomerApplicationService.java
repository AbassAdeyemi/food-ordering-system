package com.hayba.customer.service.domain.ports.input.service;

import com.hayba.customer.service.domain.create.CreateCustomerCommand;
import com.hayba.customer.service.domain.create.CreateCustomerResponse;

import javax.validation.Valid;

public interface CustomerApplicationService {

    CreateCustomerResponse createCustomer(@Valid CreateCustomerCommand createCustomerCommand);

}
