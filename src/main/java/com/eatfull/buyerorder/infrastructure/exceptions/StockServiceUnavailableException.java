package com.eatfull.buyerorder.infrastructure.exceptions;

import lombok.Getter;

@Getter
public class StockServiceUnavailableException extends RuntimeException {

    private final String code;

    public StockServiceUnavailableException() {
        super("库存服务不可用");
        this.code = "ERROR";
    }

    public StockServiceUnavailableException(String code, String message) {
        super(message);
        this.code = code;
    }
}