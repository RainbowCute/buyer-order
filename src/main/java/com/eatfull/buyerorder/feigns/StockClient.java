package com.eatfull.buyerorder.feigns;

import com.eatfull.buyerorder.feigns.dto.FoodDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "stock", url = "${feign.stock.url}")
public interface StockClient {

    @PostMapping("/reserve")
    boolean reserve(@RequestBody List<FoodDto> foodDtos);
}
