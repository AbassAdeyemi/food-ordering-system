package com.hayba.restaurant.service.domain.ports.output.repository;

import com.hayba.restaurant.service.domain.entity.OrderApproval;

public interface OrderApprovalRepository {
    OrderApproval save(OrderApproval orderApproval);
}
