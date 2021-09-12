package com.eatfull.buyerorder.controller;

import com.alibaba.fastjson.JSONObject;
import com.eatfull.buyerorder.base.IntegrationTest;
import com.eatfull.buyerorder.controller.dto.OrderCreationRequestDto;
import com.eatfull.buyerorder.controller.dto.OrderItemRequestDto;
import com.eatfull.buyerorder.infrastructure.exceptions.OrderCancelFailedException;
import com.eatfull.buyerorder.infrastructure.exceptions.OrderCreationFailedException;
import com.eatfull.buyerorder.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderControllerTest extends IntegrationTest {

    @MockBean
    OrderService orderService;

    @Test
    void should_cancel_order_success_when_cancel_order_given_order_id_and_food_preparing_status_and_food_preparation_time_and_acceptance_order_time() throws Exception {
        Long orderId = 1L;
        Mockito.when(orderService.cancelOrder(orderId)).thenReturn(true);

        mockMvc.perform(post("/buyer/orders/{orderId}/cancellation-confirmation", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("SUCCESS")))
                .andExpect(jsonPath("$.message", is("取消订单成功")));
    }

    @Test
    void should_throw_exception_when_cancel_order_given_order_service_throw_order_shipping_failed_exception() throws Exception {
        Long orderId = 1L;
        Mockito.when(orderService.cancelOrder(orderId)).thenThrow(new OrderCancelFailedException("ORDER_SHIPPING", "已开始配送，不能取消订单"));

        mockMvc.perform(post("/buyer/orders/{orderId}/cancellation-confirmation", orderId))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code", is("ORDER_SHIPPING")))
                .andExpect(jsonPath("$.message", is("已开始配送，不能取消订单")));
    }

    @Test
    void should_throw_exception_when_cancel_order_given_order_service_throw_order_preparation_not_overdue_failed_exception() throws Exception {
        Long orderId = 1L;
        Mockito.when(orderService.cancelOrder(orderId)).thenThrow(new OrderCancelFailedException("FOOD_PREPARATION_NOT_OVERDUE", "备餐未超时，不能取消订单"));

        mockMvc.perform(post("/buyer/orders/{orderId}/cancellation-confirmation", orderId))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code", is("FOOD_PREPARATION_NOT_OVERDUE")))
                .andExpect(jsonPath("$.message", is("备餐未超时，不能取消订单")));
    }

    @Test
    void should_throw_exception_when_cancel_order_given_order_service_throw_order_not_found_failed_exception() throws Exception {
        Long orderId = 1L;
        Mockito.when(orderService.cancelOrder(orderId)).thenThrow(new OrderCancelFailedException("ORDER_NOT_FOUND", "订单不存在"));

        mockMvc.perform(post("/buyer/orders/{orderId}/cancellation-confirmation", orderId))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code", is("ORDER_NOT_FOUND")))
                .andExpect(jsonPath("$.message", is("订单不存在")));
    }

    @Test
    void should_create_order_success_when_create_order_given_proposal_id_and_order_info() throws Exception {
        Long orderId = 1L;
        Mockito.when(orderService.createOrder(any())).thenReturn(orderId);
        OrderCreationRequestDto orderCreationRequestDto = OrderCreationRequestDto.builder()
                .orderItemDtos(Collections.singletonList(OrderItemRequestDto.builder()
                                                                 .foodPreparationTime(10)
                                                                 .price(BigDecimal.valueOf(20))
                                                                 .quantity(1).build()))
                .build();
        String requestJson = JSONObject.toJSONString(orderCreationRequestDto);

        mockMvc.perform(post("/buyer/food-proposals/{id}/order", 1)
                                .content(requestJson)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId", is(1)));
    }

    @Test
    void should_throw_exception_when_create_order_given_order_service_throw_order_stock_not_enough_exception() throws Exception {
        Mockito.when(orderService.createOrder(any())).thenThrow(new OrderCreationFailedException("STOCK_NOT_ENOUGH", "库存不足"));
        OrderCreationRequestDto orderCreationRequestDto = OrderCreationRequestDto.builder()
                .orderItemDtos(Collections.singletonList(OrderItemRequestDto.builder()
                                                                 .foodPreparationTime(10)
                                                                 .price(BigDecimal.valueOf(20))
                                                                 .quantity(1).build()))
                .build();
        String requestJson = JSONObject.toJSONString(orderCreationRequestDto);

        mockMvc.perform(post("/buyer/food-proposals/{id}/order", 1)
                                .content(requestJson)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code", is("STOCK_NOT_ENOUGH")))
                .andExpect(jsonPath("$.message", is("库存不足")));
    }
}
