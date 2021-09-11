package com.eatfull.buyerorder.message.dto;

import com.eatfull.buyerorder.infrastructure.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderCancellationMessage {
    private Long orderId;

    public static OrderCancellationMessage from(Order order) {
        return OrderCancellationMessage.builder()
                .orderId(order.getId())
                .build();
    }
}
