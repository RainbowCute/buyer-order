package com.eatfull.buyerorder.service;

import com.eatfull.buyerorder.enums.OrderStatus;
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
public class OrderCancellationModel {
    private OrderStatus status;
}
