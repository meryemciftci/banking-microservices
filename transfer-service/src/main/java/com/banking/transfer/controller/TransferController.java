package com.banking.transfer.controller;

import com.banking.transfer.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    // TODO: POST / - Money transfer
    // TODO: POST /between-accounts - Between own accounts
    // TODO: GET /{id}/status - Transfer status
}