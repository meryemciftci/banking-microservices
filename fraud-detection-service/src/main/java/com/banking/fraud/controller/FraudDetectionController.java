package com.banking.fraud.controller;

import com.banking.fraud.dto.BlacklistRequest;
import com.banking.fraud.dto.FraudCheckRequest;
import com.banking.fraud.entity.FraudAlert;
import com.banking.fraud.service.FraudDetectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/fraud")
@RequiredArgsConstructor
public class FraudDetectionController {

    private final FraudDetectionService fraudDetectionService;

    @PostMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkFraud(@Valid @RequestBody FraudCheckRequest request) {
        boolean isFraud = fraudDetectionService.checkFraud(
                request.getTransferId(),
                request.getSenderIban(),
                request.getReceiverIban(),
                request.getAmount()
        );
        return ResponseEntity.ok(Map.of("isFraud", isFraud));
    }

    @GetMapping("/alerts")
    public ResponseEntity<List<FraudAlert>> getAllAlerts() {
        return ResponseEntity.ok(fraudDetectionService.getAllAlerts());
    }

    @GetMapping("/alerts/pending")
    public ResponseEntity<List<FraudAlert>> getPendingAlerts() {
        return ResponseEntity.ok(fraudDetectionService.getPendingAlerts());
    }

    @PostMapping("/blacklist")
    public ResponseEntity<Map<String, String>> addToBlacklist(@Valid @RequestBody BlacklistRequest request) {
        fraudDetectionService.addToBlacklist(request.getIban(), request.getReason());
        return ResponseEntity.ok(Map.of("message", "IBAN added to blacklist"));
    }

    @DeleteMapping("/blacklist/{iban}")
    public ResponseEntity<Map<String, String>> removeFromBlacklist(@PathVariable String iban) {
        fraudDetectionService.removeFromBlacklist(iban);
        return ResponseEntity.ok(Map.of("message", "IBAN removed from blacklist"));
    }
}