package com.eatfull.buyerorder.controller;

import com.eatfull.buyerorder.base.IntegrationTest;
import com.eatfull.buyerorder.infrastructure.exceptions.OrderCancelFailedException;
import com.eatfull.buyerorder.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.hamcrest.Matchers.is;
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
}
