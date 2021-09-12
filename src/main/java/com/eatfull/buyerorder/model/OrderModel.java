package com.eatfull.buyerorder.model;

import com.eatfull.buyerorder.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderModel {
    private LocalDateTime acceptanceOrderTime;
    private OrderStatus status;
    List<OrderItemModel> orderItemModels;
}
