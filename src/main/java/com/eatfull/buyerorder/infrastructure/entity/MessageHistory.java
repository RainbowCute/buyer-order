package com.eatfull.buyerorder.infrastructure.entity;

import com.eatfull.buyerorder.enums.MessageSendStatus;
import com.eatfull.buyerorder.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class MessageHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MessageType type;

    @Enumerated(EnumType.STRING)
    private MessageSendStatus status;

    private String content;
}
