package com.hayba.restaurant.service.domain.event;

import com.hayba.domain.event.publisher.DomainEventPublisher;
import com.hayba.domain.valueobject.RestaurantId;
import com.hayba.restaurant.service.domain.entity.OrderApproval;

import java.time.ZonedDateTime;
import java.util.List;

public class OrderApprovedEvent extends OrderApprovalEvent {

    public OrderApprovedEvent(OrderApproval orderApproval,
                              RestaurantId restaurantId,
                              List<String> failureMessages,
                              ZonedDateTime createdAt) {
        super(orderApproval, restaurantId, failureMessages, createdAt);
    }

}
