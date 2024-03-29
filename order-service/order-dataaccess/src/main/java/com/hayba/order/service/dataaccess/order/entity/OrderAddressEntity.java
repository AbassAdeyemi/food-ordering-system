package com.hayba.order.service.dataaccess.order.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_address")
@Entity
public class OrderAddressEntity {
    @Id
    private UUID id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ORDER_ID")
    private OrderEntity order;

    private String street;
    private String postalCode;
    private String city;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderAddressEntity)) return false;
        OrderAddressEntity that = (OrderAddressEntity) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
