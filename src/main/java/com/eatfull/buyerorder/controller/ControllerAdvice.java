package com.eatfull.buyerorder.controller;

import com.eatfull.buyerorder.controller.dto.OrderCancellationResponseDto;
import com.eatfull.buyerorder.controller.dto.OrderCreationResponseDto;
import com.eatfull.buyerorder.infrastructure.exceptions.OrderCancelFailedException;
import com.eatfull.buyerorder.infrastructure.exceptions.OrderCreationFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(OrderCancelFailedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public OrderCancellationResponseDto handleOrderCancellationException(OrderCancelFailedException e) {
        return OrderCancellationResponseDto.builder().code(e.getCode()).message(e.getMessage()).build();
    }

    @ExceptionHandler(OrderCreationFailedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public OrderCreationResponseDto handleOrderCreationException(OrderCreationFailedException e) {
        return OrderCreationResponseDto.builder().code(e.getCode()).message(e.getMessage()).build();
    }
}
