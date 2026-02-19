package com.banking.notification.listener;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferEvent implements Serializable {

    private Long transferId;
    private String senderIban;
    private String receiverIban;
    private BigDecimal amount;
    private String description;
    private String status;
    private LocalDateTime createdAt;
}