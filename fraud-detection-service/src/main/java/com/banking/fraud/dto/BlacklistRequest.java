package com.banking.fraud.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlacklistRequest {

    @NotBlank(message = "IBAN is required")
    private String iban;

    private String reason;
}