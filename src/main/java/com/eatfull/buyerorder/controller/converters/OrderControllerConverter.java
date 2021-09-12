package com.eatfull.buyerorder.controller.converters;

import com.eatfull.buyerorder.controller.dto.OrderCreationRequestDto;
import com.eatfull.buyerorder.model.OrderItemModel;
import com.eatfull.buyerorder.model.OrderModel;

import java.util.stream.Collectors;

public class OrderControllerConverter {

    public static OrderModel toModel(OrderCreationRequestDto orderCreationRequestDto) {
        return OrderModel.builder()
                .orderItemModels(orderCreationRequestDto.getOrderItemDtos().stream()
                                         .map(item -> OrderItemModel.builder()
                                                 .quantity(item.getQuantity())
                                                 .price(item.getPrice())
                                                 .foodPreparationTime(item.getFoodPreparationTime())
                                                 .build())
                                         .collect(Collectors.toList()))
                .build();
    }
}
