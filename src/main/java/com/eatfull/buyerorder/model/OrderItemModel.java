package com.eatfull.buyerorder.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderItemModel {
    private Integer foodPreparationTime;
    private BigDecimal price;
    private Integer quantity;
    private Long id;
}
