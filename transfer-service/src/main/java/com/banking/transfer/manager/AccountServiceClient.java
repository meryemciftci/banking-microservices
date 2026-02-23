package com.banking.transfer.manager;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ACCOUNT-SERVICE")
public interface AccountServiceClient {

    @GetMapping("/api/v1/accounts/{iban}")
    ResponseEntity<AccountResponse> getAccountByIban(@PathVariable String iban);

    @PutMapping("/api/v1/accounts/balance")
    ResponseEntity<Void> updateBalance(@RequestBody UpdateBalanceRequest request);
}