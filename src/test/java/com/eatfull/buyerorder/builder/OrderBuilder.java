package com.eatfull.buyerorder.builder;

import com.eatfull.buyerorder.enums.OrderStatus;
import com.eatfull.buyerorder.infrastructure.entity.Order;
import com.eatfull.buyerorder.infrastructure.entity.OrderItem;
import com.eatfull.buyerorder.infrastructure.repository.OrderRepository;
import com.eatfull.buyerorder.utils.SpringApplicationContext;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderBuilder {
    private Order order = new Order();

    public static OrderBuilder withDefault() {
        return new OrderBuilder()
                .withId(1L)
                .withUserId(1L)
                .withOrderItems(new ArrayList<>());
    }

    public Order build() {
        return order;
    }

    public Order persist() {
        OrderRepository repository = SpringApplicationContext.getBean(OrderRepository.class);
        return repository.saveAndFlush(order);
    }

    public OrderBuilder withId(Long id) {
        order.setId(id);
        return this;
    }

    public OrderBuilder withUserId(Long userId) {
        order.setUserId(userId);
        return this;
    }

    public OrderBuilder withOrderItems(List<OrderItem> orderItems) {
        order.setOrderItems(orderItems);
        orderItems.forEach(item -> item.setOrder(order));
        return this;
    }

    public OrderBuilder withStatus(OrderStatus status) {
        order.setStatus(status);
        return this;
    }

    public OrderBuilder withAcceptanceOrderTime(LocalDateTime acceptanceOrderTime) {
        order.setAcceptanceOrderTime(acceptanceOrderTime);
        return this;
    }
}