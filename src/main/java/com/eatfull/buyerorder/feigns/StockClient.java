package com.eatfull.buyerorder.feigns;

import com.eatfull.buyerorder.feigns.dto.ReserveStockRequestDto;
import com.eatfull.buyerorder.feigns.dto.ReserveStockResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "stock", url = "${feign.stock.url}")
public interface StockClient {

    @PostMapping("/reserve")
    ReserveStockResponseDto reserve(@RequestBody List<ReserveStockRequestDto> foodDtos);
}
