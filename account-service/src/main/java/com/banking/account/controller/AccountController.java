package com.banking.account.controller;

import com.banking.account.dto.request.CreateAccountRequest;
import com.banking.account.dto.request.UpdateStatusRequest;
import com.banking.account.dto.response.AccountResponse;
import com.banking.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.banking.account.dto.request.UpdateBalanceRequest;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        return ResponseEntity.ok(accountService.createAccount(request));
    }

    @GetMapping("/{iban}")
    public ResponseEntity<AccountResponse> getAccountByIban(@PathVariable String iban) {
        return ResponseEntity.ok(accountService.getAccountByIban(iban));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AccountResponse>> getAccountsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(accountService.getAccountsByUserId(userId));
    }

    @PatchMapping("/{iban}/status")
    public ResponseEntity<AccountResponse> updateStatus(
            @PathVariable String iban,
            @Valid @RequestBody UpdateStatusRequest request) {
        return ResponseEntity.ok(accountService.updateStatus(iban, request));
    }
    @PutMapping("/balance")
    public ResponseEntity<Void> updateBalance(@Valid @RequestBody UpdateBalanceRequest request) {
        accountService.updateBalance(request.getIban(), request.getAmount());
        return ResponseEntity.ok().build();
    }
}