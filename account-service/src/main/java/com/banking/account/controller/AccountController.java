package com.banking.account.controller;

import com.banking.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    // TODO: POST / - Create account
    // TODO: GET /{iban} - Get account details
    // TODO: GET /my-accounts - List user accounts
    // TODO: PATCH /{iban}/status - Freeze/activate
}