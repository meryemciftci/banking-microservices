package com.banking.account.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBalanceRequest {

    @NotBlank(message = "IBAN is required")
    private String iban;

    @NotNull(message = "Amount is required")
    private BigDecimal amount;
}