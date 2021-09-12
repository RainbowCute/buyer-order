package com.eatfull.buyerorder.message;

import com.eatfull.buyerorder.base.IntegrationTest;
import com.eatfull.buyerorder.message.dto.OrderCancellationMessage;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MessageSenderTest extends IntegrationTest {

    @Autowired
    private MessageSender messageSender;

    @Test
    void should_return_true_whe_send_message_given_send_success_status() {
        RabbitTemplate stubRabbitTemplate = Mockito.mock(RabbitTemplate.class);
        ReflectionTestUtils.setField(messageSender, "rabbitTemplate", stubRabbitTemplate);

        boolean send = messageSender.send(OrderCancellationMessage.builder().orderId(1L).build());

        assertTrue(send);
    }

}
