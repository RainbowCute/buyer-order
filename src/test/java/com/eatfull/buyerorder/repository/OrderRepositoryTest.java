package com.eatfull.buyerorder.repository;

import com.eatfull.buyerorder.base.IntegrationTest;
import com.eatfull.buyerorder.builder.OrderBuilder;
import com.eatfull.buyerorder.enums.MessageSendStatus;
import com.eatfull.buyerorder.enums.MessageType;
import com.eatfull.buyerorder.enums.OrderStatus;
import com.eatfull.buyerorder.infrastructure.entity.MessageHistory;
import com.eatfull.buyerorder.infrastructure.entity.Order;
import com.eatfull.buyerorder.infrastructure.entity.OrderItem;
import com.eatfull.buyerorder.infrastructure.repository.MessageHistoryRepository;
import com.eatfull.buyerorder.infrastructure.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderRepositoryTest extends IntegrationTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MessageHistoryRepository messageHistoryRepository;

    @Test
    void should_update_status_success_when_update_order_status_given_order_status() {
        Order order = OrderBuilder.withDefault()
                .withId(1L)
                .withUserId(1L)
                .withAcceptanceOrderTime(LocalDateTime.now().minusMinutes(30))
                .withOrderItems(Collections.singletonList(OrderItem.builder()
                                                                  .foodPreparationTime(10)
                                                                  .price(BigDecimal.valueOf(10))
                                                                  .quantity(1)
                                                                  .build()))
                .withStatus(OrderStatus.CANCELED)
                .build();

        Order savedOrder = orderRepository.save(order);

        assertEquals(OrderStatus.CANCELED, savedOrder.getStatus());
    }

    @Test
    void should_save_message_history_success_when_send_message_fail() {
        MessageHistory messageHistory = MessageHistory.builder()
                .id(1L)
                .type(MessageType.ORDER_CANCELLATION)
                .status(MessageSendStatus.SEND_FAIL)
                .content("111")
                .build();

        MessageHistory savedMessageHistory = messageHistoryRepository.save(messageHistory);

        assertEquals(MessageSendStatus.SEND_FAIL, savedMessageHistory.getStatus());
        assertEquals(MessageType.ORDER_CANCELLATION, savedMessageHistory.getType());
    }
}
