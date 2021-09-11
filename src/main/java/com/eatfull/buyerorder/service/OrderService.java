package com.eatfull.buyerorder.service;

import com.eatfull.buyerorder.enums.OrderStatus;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    public OrderCancellationModel cancelOrder(Long id) {


        return OrderCancellationModel.builder().status(OrderStatus.CANCELED).build();
    }
}
