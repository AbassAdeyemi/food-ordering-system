package com.hayba.order.service.domain.valueobject;

import com.hayba.domain.valueobject.BaseId;

public class OrderItemId extends BaseId<Long> {
    public OrderItemId(Long value) {
        super(value);
    }
}
