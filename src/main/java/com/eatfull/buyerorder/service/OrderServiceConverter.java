package com.eatfull.buyerorder.service;

import com.eatfull.buyerorder.enums.OrderStatus;
import com.eatfull.buyerorder.feigns.dto.FoodDto;
import com.eatfull.buyerorder.infrastructure.entity.Order;
import com.eatfull.buyerorder.infrastructure.entity.OrderItem;
import com.eatfull.buyerorder.model.OrderModel;

import java.util.List;
import java.util.stream.Collectors;

public class OrderServiceConverter {

    public static List<FoodDto> toFoodDto(OrderModel orderModel) {
        return orderModel.getOrderItemModels().stream()
                .map(item -> FoodDto.builder()
                        .id(item.getId())
                        .quantity(item.getQuantity())
                        .build())
                .collect(Collectors.toList());
    }

    public static Order toEntity(OrderModel orderModel) {
        return Order.builder()
                .acceptanceOrderTime(orderModel.getAcceptanceOrderTime())
                .status(OrderStatus.GENERATED)
                .orderItems(orderModel.getOrderItemModels().stream()
                                    .map(item -> OrderItem.builder()
                                            .quantity(item.getQuantity())
                                            .foodPreparationTime(item.getFoodPreparationTime())
                                            .price(item.getPrice())
                                            .build())
                                    .collect(Collectors.toList()))
                .build();
    }
}
