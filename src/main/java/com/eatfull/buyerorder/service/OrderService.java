package com.eatfull.buyerorder.service;

import com.alibaba.fastjson.JSONObject;
import com.eatfull.buyerorder.enums.MessageSendStatus;
import com.eatfull.buyerorder.enums.MessageType;
import com.eatfull.buyerorder.enums.OrderStatus;
import com.eatfull.buyerorder.feigns.StockClient;
import com.eatfull.buyerorder.feigns.dto.ReserveStockRequestDto;
import com.eatfull.buyerorder.feigns.dto.ReserveStockResponseDto;
import com.eatfull.buyerorder.infrastructure.entity.MessageHistory;
import com.eatfull.buyerorder.infrastructure.entity.Order;
import com.eatfull.buyerorder.infrastructure.entity.OrderItem;
import com.eatfull.buyerorder.infrastructure.exceptions.OrderCancelFailedException;
import com.eatfull.buyerorder.infrastructure.exceptions.OrderCreationFailedException;
import com.eatfull.buyerorder.infrastructure.exceptions.StockServiceUnavailableException;
import com.eatfull.buyerorder.infrastructure.repository.MessageHistoryRepository;
import com.eatfull.buyerorder.infrastructure.repository.OrderRepository;
import com.eatfull.buyerorder.message.MessageSender;
import com.eatfull.buyerorder.message.dto.OrderCancellationMessage;
import com.eatfull.buyerorder.model.OrderModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final MessageHistoryRepository messageHistoryRepository;
    private final MessageSender messageSender;
    private final StockClient stockClient;

    public boolean cancelOrder(Long id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (!orderOptional.isPresent()) {
            throw new OrderCancelFailedException("ORDER_NOT_FOUND", "订单不存在");
        }
        Order order = orderOptional.get();
        if (order.getStatus() == OrderStatus.SHIPPING) {
            throw new OrderCancelFailedException("ORDER_SHIPPING", "已开始配送，不能取消订单");
        }
        if (order.getStatus() == OrderStatus.PREPARING && !isPastFoodPreparationTime(order)) {
            throw new OrderCancelFailedException("FOOD_PREPARATION_NOT_OVERDUE", "备餐未超时，不能取消订单");
        }
        if (order.getStatus() == OrderStatus.PREPARING && isPastFoodPreparationTime(order)) {
            OrderCancellationMessage orderCancellationMessage = OrderCancellationMessage.from(order);
            boolean sendResult = messageSender.send(orderCancellationMessage);
            if (!sendResult) {
                saveMessage(orderCancellationMessage);
            }
            order.setStatus(OrderStatus.CANCELED);
            orderRepository.save(order);
            return true;
        }
        throw new OrderCancelFailedException();
    }


    private boolean isPastFoodPreparationTime(Order order) {
        Integer maxFoodPreparationTime = order.getOrderItems().stream()
                .map(OrderItem::getFoodPreparationTime)
                .max(Integer::compare)
                .orElse(0);
        return LocalDateTime.now().isAfter(order.getAcceptanceOrderTime().plusMinutes(maxFoodPreparationTime));
    }

    private void saveMessage(OrderCancellationMessage orderCancellationMessage) {
        messageHistoryRepository.save(MessageHistory.builder()
                                              .status(MessageSendStatus.SEND_FAIL)
                                              .type(MessageType.ORDER_CANCELLATION)
                                              .content(JSONObject.toJSONString(orderCancellationMessage))
                                              .build());
    }

    public Long createOrder(OrderModel orderModel) {
        boolean reserve = reserveStock(OrderServiceConverter.toFoodDto(orderModel));
        if (!reserve) {
            throw new OrderCreationFailedException("STOCK_NOT_ENOUGH", "库存不足");
        }
        Order savedOrder = orderRepository.save(OrderServiceConverter.toEntity(orderModel));
        return savedOrder.getId();
    }

    private boolean reserveStock(List<ReserveStockRequestDto> reserveStockRequestDtos) {
        try {
            ReserveStockResponseDto reserveStockResponseDto = stockClient.reserve(reserveStockRequestDtos);
            return reserveStockResponseDto.isSuccess();
        } catch (Exception e) {
            log.error("stock service unavailable: ", e);
            throw new StockServiceUnavailableException("STOCK_SERVICE_UNAVAILABLE", "库存服务不可用，请稍后再试");
        }
    }

}
