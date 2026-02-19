package com.banking.transaction.controller;

import com.banking.transaction.entity.Transaction;
import com.banking.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/account/{iban}")
    public ResponseEntity<List<Transaction>> getTransactionsByIban(@PathVariable String iban) {
        return ResponseEntity.ok(transactionService.getTransactionsByIban(iban));
    }
}