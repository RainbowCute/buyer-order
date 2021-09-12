package com.eatfull.buyerorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BuyerOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(BuyerOrderApplication.class, args);
    }
}
