package com.banking.transfer.manager;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FraudCheckRequest {
    private Long transferId;
    private String senderIban;
    private String receiverIban;
    private BigDecimal amount;
}