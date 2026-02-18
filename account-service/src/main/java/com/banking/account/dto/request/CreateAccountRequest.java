package com.banking.account.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Account holder name is required")
    private String accountHolderName;

    private String accountType;
}