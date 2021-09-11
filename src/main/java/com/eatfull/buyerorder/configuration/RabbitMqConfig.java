package com.eatfull.buyerorder.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.eatfull.buyerorder.message.MessageConstants.EXCHANGE_NAME;
import static com.eatfull.buyerorder.message.MessageConstants.QUEUE_NAME;
import static com.eatfull.buyerorder.message.MessageConstants.ROUNTING_KEY;

@Configuration
public class RabbitMqConfig {

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue orderTicketQueue() {
        return new Queue(QUEUE_NAME);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(orderTicketQueue()).to(directExchange()).with(ROUNTING_KEY);
    }

}