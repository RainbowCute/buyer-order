package com.eatfull.buyerorder.infrastructure.repository;

import com.eatfull.buyerorder.infrastructure.entity.MessageHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageHistoryRepository extends JpaRepository<MessageHistory, Long> {
}
