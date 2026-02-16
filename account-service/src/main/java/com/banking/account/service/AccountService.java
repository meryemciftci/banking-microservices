package com.banking.account.service;

import com.banking.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    // TODO: Create account
    // TODO: Get account by IBAN
    // TODO: Get accounts by userId
    // TODO: Update account status
    // TODO: Update balance
}