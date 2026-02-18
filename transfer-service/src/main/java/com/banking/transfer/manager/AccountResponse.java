package com.banking.transfer.manager;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {
    private Long id;
    private String iban;
    private Long userId;
    private String accountHolderName;
    private String accountType;
    private BigDecimal balance;
    private String status;
    private LocalDateTime createdAt;
}