package com.hayba.order.service.domain.dto.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class CreateOrderCommand {
    @NotNull
    private final UUID customerId;
    @NotNull
    private final UUID restaurantId;
    @NotNull
    private final BigDecimal price;
    @NotNull
    private final OrderAddress orderAddress;
    @NotNull
    private final List<OrderItem> items;

}
