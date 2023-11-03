package com.hayba.rabbitmq.order.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Product {
    private String id;
    private int quantity;
}
