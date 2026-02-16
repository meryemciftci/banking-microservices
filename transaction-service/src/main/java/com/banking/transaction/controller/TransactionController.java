package com.banking.transaction.controller;

import com.banking.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    // TODO: GET / - Transaction history (filtered)
    // TODO: GET /report - Monthly/yearly report
}