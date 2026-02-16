package com.banking.transaction.service;

import com.banking.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    // TODO: Save transaction (RabbitMQ listener)
    // TODO: Get transactions by IBAN
    // TODO: Get transactions by date range (report)
}