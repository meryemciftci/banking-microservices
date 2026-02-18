package com.banking.transfer.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferResponse {

    private Long id;
    private String senderIban;
    private String receiverIban;
    private BigDecimal amount;
    private String description;
    private String status;
    private LocalDateTime createdAt;
}