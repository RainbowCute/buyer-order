package com.eatfull.buyerorder.message;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageConstants {
    public static final String EXCHANGE_NAME = "buyer-order-excahnge";
    public static final String ROUNTING_KEY = "buyer-order-rounting-key";
    public static final String QUEUE_NAME = "buyer-order-queue";
}
