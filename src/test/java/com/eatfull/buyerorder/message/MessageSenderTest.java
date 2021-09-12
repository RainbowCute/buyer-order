package com.eatfull.buyerorder.message;

import com.eatfull.buyerorder.base.IntegrationTest;
import com.eatfull.buyerorder.message.dto.OrderCancellationMessage;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

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

    @Test
    void should_return_true_whe_send_message_given_rabbit_template_throw_exception() {
        RabbitTemplate stubRabbitTemplate = Mockito.mock(RabbitTemplate.class);
        ReflectionTestUtils.setField(messageSender, "rabbitTemplate", stubRabbitTemplate);
        Mockito.doThrow(AmqpException.class).when(stubRabbitTemplate).convertAndSend(anyString(), anyString(), java.util.Optional.ofNullable(any()));

        boolean send = messageSender.send(OrderCancellationMessage.builder().orderId(1L).build());

        assertFalse(send);
    }
}
