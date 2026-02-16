package com.banking.fraud.controller;

import com.banking.fraud.service.FraudDetectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/fraud")
@RequiredArgsConstructor
public class FraudDetectionController {

    private final FraudDetectionService fraudDetectionService;

    // TODO: GET /alerts - List fraud alerts (ADMIN)
    // TODO: POST /blacklist - Add IBAN to blacklist (ADMIN)
    // TODO: DELETE /blacklist/{iban} - Remove from blacklist (ADMIN)
}