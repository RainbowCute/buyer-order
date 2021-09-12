package com.eatfull.buyerorder.infrastructure.exceptions;

import lombok.Getter;

@Getter
public class OrderCreationFailedException extends RuntimeException {

    private final String code;

    public OrderCreationFailedException() {
        super("创建订单失败");
        this.code = "ERROR";
    }

    public OrderCreationFailedException(String code, String message) {
        super(message);
        this.code = code;
    }
}
