package com.banking.fraud.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FraudCheckRequest {

    @NotNull(message = "Transfer ID is required")
    private Long transferId;

    @NotBlank(message = "Sender IBAN is required")
    private String senderIban;

    @NotBlank(message = "Receiver IBAN is required")
    private String receiverIban;

    @NotNull(message = "Amount is required")
    private BigDecimal amount;
}