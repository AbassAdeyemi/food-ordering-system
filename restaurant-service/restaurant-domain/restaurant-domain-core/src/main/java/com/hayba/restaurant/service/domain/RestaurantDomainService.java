package com.hayba.restaurant.service.domain;

import com.hayba.domain.event.publisher.DomainEventPublisher;
import com.hayba.restaurant.service.domain.entity.Restaurant;
import com.hayba.restaurant.service.domain.event.OrderApprovalEvent;
import com.hayba.restaurant.service.domain.event.OrderApprovedEvent;
import com.hayba.restaurant.service.domain.event.OrderRejectedEvent;

import java.util.List;

public interface RestaurantDomainService {

    OrderApprovalEvent validateOrder(Restaurant restaurant,
                                     List<String> failureMessages);
}
