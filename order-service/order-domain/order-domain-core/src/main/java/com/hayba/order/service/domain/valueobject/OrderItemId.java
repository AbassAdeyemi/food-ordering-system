package com.hayba.order.service.domain.valueobject;

import com.hayba.domain.valueobject.BaseId;
import com.hayba.domain.valueobject.OrderId;

import java.util.UUID;

public class OrderItemId extends BaseId<Long> {
    public OrderItemId(Long value) {
        super(value);
    }
}
