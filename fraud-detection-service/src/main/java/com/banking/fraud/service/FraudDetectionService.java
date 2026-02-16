package com.banking.fraud.service;

import com.banking.fraud.repository.BlacklistedIbanRepository;
import com.banking.fraud.repository.FraudAlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FraudDetectionService {

    private final FraudAlertRepository fraudAlertRepository;
    private final BlacklistedIbanRepository blacklistedIbanRepository;

    // TODO: Check transfer for fraud
    // TODO: Get all alerts (ADMIN)
    // TODO: Add IBAN to blacklist (ADMIN)
    // TODO: Remove from blacklist (ADMIN)
}