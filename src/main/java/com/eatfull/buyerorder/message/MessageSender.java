package com.eatfull.buyerorder.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    ObjectMapper objectMapper;

    public boolean send(Object messageObject) {
        try {
            String message = objectMapper.writeValueAsString(messageObject);
            rabbitTemplate.convertAndSend(MessageConstants.EXCHANGE_NAME, MessageConstants.ROUNTING_KEY, message);
        } catch (Exception e) {
            log.error("send message error {}", messageObject, e);
            return false;
        }
        return true;
    }
}
