package com.eatfull.buyerorder.controller;

import com.eatfull.buyerorder.controller.dto.OrderCancellationResponseDto;
import com.eatfull.buyerorder.infrastructure.exceptions.OrderCancelFailedException;
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
}
