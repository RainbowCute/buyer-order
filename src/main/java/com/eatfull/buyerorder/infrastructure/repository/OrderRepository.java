package com.eatfull.buyerorder.infrastructure.repository;

import com.eatfull.buyerorder.infrastructure.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
