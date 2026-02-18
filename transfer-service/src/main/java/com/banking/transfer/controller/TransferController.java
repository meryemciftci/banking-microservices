package com.banking.transfer.controller;

import com.banking.transfer.dto.request.TransferRequest;
import com.banking.transfer.dto.response.TransferResponse;
import com.banking.transfer.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<TransferResponse> transfer(@Valid @RequestBody TransferRequest request) {
        return ResponseEntity.ok(transferService.transfer(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransferResponse> getTransferById(@PathVariable Long id) {
        return ResponseEntity.ok(transferService.getTransferById(id));
    }

    @GetMapping("/account/{iban}")
    public ResponseEntity<List<TransferResponse>> getTransfersByIban(@PathVariable String iban) {
        return ResponseEntity.ok(transferService.getTransfersByIban(iban));
    }
}