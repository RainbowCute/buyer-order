package com.eatfull.buyerorder.controller;


import com.eatfull.buyerorder.controller.dto.OrderCancellationResponseDto;
import com.eatfull.buyerorder.enums.OrderStatus;
import com.eatfull.buyerorder.service.OrderCancellationModel;
import com.eatfull.buyerorder.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/buyer/")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders/{id}/cancellation-confirmation")
    public OrderCancellationResponseDto cancelOrder(@PathVariable("id") Long id) {
        OrderCancellationModel orderCancellationModel = orderService.cancelOrder(id);
        if (orderCancellationModel.getStatus() == OrderStatus.CANCELED) {
            return OrderCancellationResponseDto.builder().code("SUCCESS").message("取消订单成功").build();
        }
        return null;
    }
}
