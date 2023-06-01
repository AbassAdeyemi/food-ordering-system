package com.hayba.order.service.domain.ports.input.service;

import com.hayba.order.service.domain.dto.create.CreateOrderCommand;
import com.hayba.order.service.domain.dto.create.CreateOrderResponse;
import com.hayba.order.service.domain.dto.track.TrackOrderQuery;
import com.hayba.order.service.domain.dto.track.TrackOrderResponse;

import javax.validation.Valid;

public interface OrderApplicationService {

    CreateOrderResponse createOrder(@Valid CreateOrderCommand createOrderCommand);

    TrackOrderResponse trackOrder(@Valid TrackOrderQuery trackOrderQuery);


}
