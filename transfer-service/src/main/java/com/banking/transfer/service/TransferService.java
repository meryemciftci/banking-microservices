package com.banking.transfer.service;

import com.banking.transfer.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final TransferRepository transferRepository;

    // TODO: Transfer money
    // TODO: Transfer between own accounts
    // TODO: Get transfer status
}