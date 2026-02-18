package com.banking.transfer.manager;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBalanceRequest {
    private String iban;
    private BigDecimal amount;
}