package com.eatfull.buyerorder.service;

import com.eatfull.buyerorder.enums.OrderStatus;
import com.eatfull.buyerorder.feigns.dto.ReserveStockRequestDto;
import com.eatfull.buyerorder.infrastructure.entity.Order;
import com.eatfull.buyerorder.infrastructure.entity.OrderItem;
import com.eatfull.buyerorder.model.OrderModel;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrderServiceConverter {

    public static List<ReserveStockRequestDto> toFoodDto(OrderModel orderModel) {
        return orderModel.getOrderItemModels().stream()
                .map(item -> ReserveStockRequestDto.builder()
                        .id(item.getId())
                        .quantity(item.getQuantity())
                        .build())
                .collect(Collectors.toList());
    }

    public static Order toEntity(OrderModel orderModel) {
        List<OrderItem> orderItems = orderModel.getOrderItemModels().stream()
                .map(item -> OrderItem.builder()
                        .quantity(item.getQuantity())
                        .foodPreparationTime(item.getFoodPreparationTime())
                        .price(item.getPrice())
                        .build())
                .collect(Collectors.toList());
        Order order = Order.builder()
                .userId(1L)
                .acceptanceOrderTime(orderModel.getAcceptanceOrderTime())
                .status(Optional.ofNullable(orderModel.getStatus()).orElse(OrderStatus.GENERATED))
                .orderItems(orderItems)
                .build();
        orderItems.forEach(item -> item.setOrder(order));
        return order;

    }
}
