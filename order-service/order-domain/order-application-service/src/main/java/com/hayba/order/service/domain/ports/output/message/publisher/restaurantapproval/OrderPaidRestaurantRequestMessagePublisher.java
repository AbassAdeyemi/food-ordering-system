package com.hayba.order.service.domain.ports.output.message.publisher.restaurantapproval;

import com.hayba.domain.event.publisher.DomainEventPublisher;
import com.hayba.order.service.domain.event.OrderPaidEvent;

public interface OrderPaidRestaurantRequestMessagePublisher extends DomainEventPublisher<OrderPaidEvent> {
}
