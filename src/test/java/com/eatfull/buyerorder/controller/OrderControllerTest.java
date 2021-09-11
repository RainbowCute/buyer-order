package com.eatfull.buyerorder.controller;

import com.eatfull.buyerorder.base.IntegrationTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderControllerTest extends IntegrationTest {

    @Test
    void should_cancel_order_success_when_cancel_order_given_order_id_and_food_preparing_status_and_food_preparation_time_and_acceptance_order_time() throws Exception {
        int orderId = 1;
        int foodPreparationTime = 30;
        LocalDateTime acceptanceOrderTime = LocalDateTime.now().minusMinutes(30);

        mockMvc.perform(post("/buyer/orders/{orderId}/cancellation-confirmation", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("SUCCESS")))
                .andExpect(jsonPath("$.message", is("取消订单成功")));
    }
}
