package com.eatfull.buyerorder.controller;


import com.eatfull.buyerorder.controller.converters.OrderControllerConverter;
import com.eatfull.buyerorder.controller.dto.OrderCancellationResponseDto;
import com.eatfull.buyerorder.controller.dto.OrderCreationRequestDto;
import com.eatfull.buyerorder.controller.dto.OrderCreationResponseDto;
import com.eatfull.buyerorder.infrastructure.exceptions.OrderCancelFailedException;
import com.eatfull.buyerorder.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@AllArgsConstructor
@RestController
@RequestMapping("/buyer/")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders/{id}/cancellation-confirmation")
    public OrderCancellationResponseDto cancelOrder(@PathVariable("id") Long id) {
        boolean cancelOrder = orderService.cancelOrder(id);
        if (cancelOrder) {
            return OrderCancellationResponseDto.builder().code("SUCCESS").message("取消订单成功").build();
        }
        throw new OrderCancelFailedException();
    }

    @PostMapping("/food-proposals/{id}/order")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderCreationResponseDto createOrder(@RequestBody OrderCreationRequestDto orderCreationRequestDto, @PathVariable String id) {
        Long orderId = orderService.createOrder(OrderControllerConverter.toModel(orderCreationRequestDto));
        if (Objects.nonNull(orderId)) {
            return OrderCreationResponseDto.builder().orderId(orderId).build();
        }
        return null;
    }
}
