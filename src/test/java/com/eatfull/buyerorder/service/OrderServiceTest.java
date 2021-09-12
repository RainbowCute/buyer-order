package com.eatfull.buyerorder.service;

import com.eatfull.buyerorder.base.IntegrationTest;
import com.eatfull.buyerorder.builder.OrderBuilder;
import com.eatfull.buyerorder.enums.MessageSendStatus;
import com.eatfull.buyerorder.enums.MessageType;
import com.eatfull.buyerorder.enums.OrderStatus;
import com.eatfull.buyerorder.feigns.StockClient;
import com.eatfull.buyerorder.infrastructure.entity.MessageHistory;
import com.eatfull.buyerorder.infrastructure.entity.OrderItem;
import com.eatfull.buyerorder.infrastructure.exceptions.OrderCancelFailedException;
import com.eatfull.buyerorder.infrastructure.exceptions.OrderCreationFailedException;
import com.eatfull.buyerorder.infrastructure.repository.MessageHistoryRepository;
import com.eatfull.buyerorder.infrastructure.repository.OrderRepository;
import com.eatfull.buyerorder.message.MessageSender;
import com.eatfull.buyerorder.model.OrderModel;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

public class OrderServiceTest extends IntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MessageHistoryRepository messageHistoryRepository;

    @Test
    void should_cancel_order_success_when_cancel_order_given_order_id_and_food_preparing_status_and_food_preparation_time_and_acceptance_order_time() {
        Long orderId = 1L;
        OrderRepository stubOrderRepository = Mockito.mock(OrderRepository.class);
        MessageSender stubMessageSender = Mockito.mock(MessageSender.class);
        ReflectionTestUtils.setField(orderService, "orderRepository", stubOrderRepository);
        ReflectionTestUtils.setField(orderService, "messageSender", stubMessageSender);
        Mockito.when(stubOrderRepository.findById(orderId)).thenReturn(Optional.of(OrderBuilder.withDefault()
                                                                                           .withId(orderId)
                                                                                           .withAcceptanceOrderTime(LocalDateTime.now().minusMinutes(30))
                                                                                           .withOrderItems(Collections.singletonList(OrderItem.builder().foodPreparationTime(20).build()))
                                                                                           .withStatus(OrderStatus.PREPARING)
                                                                                           .build()));
        Mockito.when(stubMessageSender.send(any())).thenReturn(true);

        boolean cancelOrder = orderService.cancelOrder(orderId);

        assertTrue(cancelOrder);
    }

    @Test
    void should_throw_exception_when_cancel_order_given_order_shipping_status() {
        Long orderId = 1L;
        OrderRepository stubOrderRepository = Mockito.mock(OrderRepository.class);
        MessageSender stubMessageSender = Mockito.mock(MessageSender.class);
        ReflectionTestUtils.setField(orderService, "orderRepository", stubOrderRepository);
        ReflectionTestUtils.setField(orderService, "messageSender", stubMessageSender);
        Mockito.when(stubOrderRepository.findById(orderId)).thenReturn(Optional.of(OrderBuilder.withDefault()
                                                                                           .withId(orderId)
                                                                                           .withAcceptanceOrderTime(LocalDateTime.now().minusMinutes(30))
                                                                                           .withOrderItems(Collections.singletonList(OrderItem.builder().foodPreparationTime(20).build()))
                                                                                           .withStatus(OrderStatus.SHIPPING)
                                                                                           .build()));
        Mockito.when(stubMessageSender.send(any())).thenReturn(true);

        OrderCancelFailedException orderCancelFailedException = assertThrows(OrderCancelFailedException.class, () -> orderService.cancelOrder(orderId));
        assertEquals("已开始配送，不能取消订单", orderCancelFailedException.getMessage());
    }

    @Test
    void should_throw_exception_when_cancel_order_given_order_preparing_status_and_not_past_preparation_time() {
        Long orderId = 1L;
        OrderRepository stubOrderRepository = Mockito.mock(OrderRepository.class);
        MessageSender stubMessageSender = Mockito.mock(MessageSender.class);
        ReflectionTestUtils.setField(orderService, "orderRepository", stubOrderRepository);
        ReflectionTestUtils.setField(orderService, "messageSender", stubMessageSender);
        Mockito.when(stubOrderRepository.findById(orderId)).thenReturn(Optional.of(OrderBuilder.withDefault()
                                                                                           .withId(orderId)
                                                                                           .withAcceptanceOrderTime(LocalDateTime.now().minusMinutes(10))
                                                                                           .withOrderItems(Collections.singletonList(OrderItem.builder().foodPreparationTime(20).build()))
                                                                                           .withStatus(OrderStatus.PREPARING)
                                                                                           .build()));
        Mockito.when(stubMessageSender.send(any())).thenReturn(true);

        OrderCancelFailedException orderCancelFailedException = assertThrows(OrderCancelFailedException.class, () -> orderService.cancelOrder(orderId));
        assertEquals("备餐未超时，不能取消订单", orderCancelFailedException.getMessage());

    }

    @Test
    void should_throw_exception_when_cancel_order_given_not_exist_order_id() {
        Long orderId = 1L;
        OrderRepository stubOrderRepository = Mockito.mock(OrderRepository.class);
        MessageSender stubMessageSender = Mockito.mock(MessageSender.class);
        ReflectionTestUtils.setField(orderService, "orderRepository", stubOrderRepository);
        ReflectionTestUtils.setField(orderService, "messageSender", stubMessageSender);
        Mockito.when(stubOrderRepository.findById(orderId + 1)).thenReturn(Optional.of(OrderBuilder.withDefault()
                                                                                               .withId(orderId)
                                                                                               .withAcceptanceOrderTime(LocalDateTime.now().minusMinutes(10))
                                                                                               .withOrderItems(Collections.singletonList(OrderItem.builder().foodPreparationTime(20).build()))
                                                                                               .withStatus(OrderStatus.SHIPPING)
                                                                                               .build()));
        Mockito.when(stubMessageSender.send(any())).thenReturn(true);

        OrderCancelFailedException orderCancelFailedException = assertThrows(OrderCancelFailedException.class, () -> orderService.cancelOrder(orderId));
        assertEquals("订单不存在", orderCancelFailedException.getMessage());
    }

    @Test
    void should_cancel_order_success_when_cancel_order_given_order_id_and_food_preparing_status_and_food_preparation_time_and_acceptance_order_time_and_message_consume_fail() {
        Long orderId = 1L;
        OrderRepository stubOrderRepository = Mockito.mock(OrderRepository.class);
        MessageSender stubMessageSender = Mockito.mock(MessageSender.class);
        ReflectionTestUtils.setField(orderService, "orderRepository", stubOrderRepository);
        ReflectionTestUtils.setField(orderService, "messageSender", stubMessageSender);
        Mockito.when(stubOrderRepository.findById(orderId)).thenReturn(Optional.of(OrderBuilder.withDefault()
                                                                                           .withId(orderId)
                                                                                           .withAcceptanceOrderTime(LocalDateTime.now().minusMinutes(30))
                                                                                           .withOrderItems(Collections.singletonList(OrderItem.builder().foodPreparationTime(20).build()))
                                                                                           .withStatus(OrderStatus.PREPARING)
                                                                                           .build()));
        Mockito.when(stubMessageSender.send(any())).thenReturn(false);

        boolean cancelOrder = orderService.cancelOrder(orderId);

        List<MessageHistory> messageHistories = messageHistoryRepository.findAll();
        assertTrue(cancelOrder);
        assertEquals(1, messageHistories.size());
        assertEquals(MessageType.ORDER_CANCELLATION, messageHistories.get(0).getType());
        assertEquals(MessageSendStatus.SEND_FAIL, messageHistories.get(0).getStatus());
    }

    @Test
    void should_return_order_id_when_create_order_given_order_info_and_stock_client_return_true() {
        Long orderId = 1L;
        OrderRepository stubOrderRepository = Mockito.mock(OrderRepository.class);
        StockClient stubStockClient = Mockito.mock(StockClient.class);
        ReflectionTestUtils.setField(orderService, "orderRepository", stubOrderRepository);
        ReflectionTestUtils.setField(orderService, "stockClient", stubStockClient);
        Mockito.when(stubOrderRepository.save(any())).thenReturn(OrderBuilder.withDefault()
                                                                         .withId(orderId)
                                                                         .build());
        Mockito.when(stubStockClient.reserve(any())).thenReturn(true);
        OrderModel orderModel = OrderModel.builder().orderItemModels(new ArrayList<>()).build();

        Long savedOrderId = orderService.createOrder(orderModel);

        assertEquals(1, savedOrderId.longValue());

    }

    @Test
    void should_throw_exception_when_create_order_given_stock_not_enough() {
        StockClient stubStockClient = Mockito.mock(StockClient.class);
        ReflectionTestUtils.setField(orderService, "stockClient", stubStockClient);
        Mockito.when(stubStockClient.reserve(any())).thenReturn(false);
        OrderModel orderModel = OrderModel.builder().orderItemModels(new ArrayList<>()).build();

        OrderCreationFailedException orderCreationFailedException = assertThrows(OrderCreationFailedException.class, () -> orderService.createOrder(orderModel));
        assertEquals("库存不足", orderCreationFailedException.getMessage());
    }
}
