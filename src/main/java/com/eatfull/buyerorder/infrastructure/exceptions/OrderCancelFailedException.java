package com.eatfull.buyerorder.infrastructure.exceptions;

import lombok.Getter;

@Getter
public class OrderCancelFailedException extends RuntimeException {

    private final String code;

    public OrderCancelFailedException() {
        super("取消订单失败");
        this.code = "ERROR";
    }

    public OrderCancelFailedException(String code, String message) {
        super(message);
        this.code = code;
    }
}
