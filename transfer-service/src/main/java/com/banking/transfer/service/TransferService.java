package com.banking.transfer.service;

import com.banking.transfer.dto.request.TransferRequest;
import com.banking.transfer.dto.response.TransferResponse;
import com.banking.transfer.entity.Transfer;
import com.banking.transfer.entity.TransferStatus;
import com.banking.transfer.manager.AccountResponse;
import com.banking.transfer.manager.AccountServiceClient;
import com.banking.transfer.manager.UpdateBalanceRequest;
import com.banking.transfer.repository.TransferRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import com.banking.transfer.config.RabbitMQConfig;
import com.banking.transfer.dto.event.TransferEvent;
import com.banking.transfer.manager.FraudCheckRequest;
import com.banking.transfer.manager.FraudServiceClient;
import java.util.Map;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferService {

    private final TransferRepository transferRepository;
    private final AccountServiceClient accountServiceClient;
    private final RabbitTemplate rabbitTemplate;
    private final FraudServiceClient fraudServiceClient;
    @Transactional
    @CircuitBreaker(name = "accountService", fallbackMethod = "transferFallback")
    public TransferResponse transfer(TransferRequest request) {
        // 1. Validate sender and receiver are different
        if (request.getSenderIban().equals(request.getReceiverIban())) {
            throw new RuntimeException("Sender and receiver cannot be the same");
        }

        // 2. Get sender account and validate
        AccountResponse sender = accountServiceClient.getAccountByIban(request.getSenderIban()).getBody();
        if (sender == null) {
            throw new RuntimeException("Sender account not found");
        }
        if (!"ACTIVE".equals(sender.getStatus())) {
            throw new RuntimeException("Sender account is not active");
        }
        if (sender.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        // 3. Get receiver account and validate
        AccountResponse receiver = accountServiceClient.getAccountByIban(request.getReceiverIban()).getBody();
        if (receiver == null) {
            throw new RuntimeException("Receiver account not found");
        }
        if (!"ACTIVE".equals(receiver.getStatus())) {
            throw new RuntimeException("Receiver account is not active");
        }

        // 4. Create transfer record
        Transfer transfer = Transfer.builder()
                .senderIban(request.getSenderIban())
                .receiverIban(request.getReceiverIban())
                .amount(request.getAmount())
                .description(request.getDescription())
                .status(TransferStatus.PENDING)
                .build();
        transferRepository.save(transfer);

        // 5. Fraud check
        try {
            FraudCheckRequest fraudRequest = FraudCheckRequest.builder()
                    .transferId(transfer.getId())
                    .senderIban(request.getSenderIban())
                    .receiverIban(request.getReceiverIban())
                    .amount(request.getAmount())
                    .build();

            Map<String, Boolean> fraudResult = fraudServiceClient.checkFraud(fraudRequest).getBody();
            if (fraudResult != null && fraudResult.get("isFraud")) {
                transfer.setStatus(TransferStatus.REJECTED);
                transferRepository.save(transfer);
                return toResponse(transfer);
            }
        } catch (Exception e) {
            log.warn("Fraud service unavailable, proceeding with transfer: {}", e.getMessage());
        }
        try {
            // 5. Deduct from sender
            accountServiceClient.updateBalance(UpdateBalanceRequest.builder()
                    .iban(request.getSenderIban())
                    .amount(request.getAmount().negate())
                    .build());

            // 6. Add to receiver
            accountServiceClient.updateBalance(UpdateBalanceRequest.builder()
                    .iban(request.getReceiverIban())
                    .amount(request.getAmount())
                    .build());

            // 7. Update transfer status
            transfer.setStatus(TransferStatus.COMPLETED);
            transferRepository.save(transfer);
            // 8. Send events to RabbitMQ
            TransferEvent event = TransferEvent.builder()
                    .transferId(transfer.getId())
                    .senderIban(transfer.getSenderIban())
                    .receiverIban(transfer.getReceiverIban())
                    .amount(transfer.getAmount())
                    .description(transfer.getDescription())
                    .status(transfer.getStatus().name())
                    .createdAt(transfer.getCreatedAt())
                    .build();

            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.TRANSACTION_ROUTING_KEY, event);
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.NOTIFICATION_ROUTING_KEY, event);

            log.info("Transfer events sent to RabbitMQ");

            log.info("Transfer completed: {} -> {} amount: {}",
                    request.getSenderIban(), request.getReceiverIban(), request.getAmount());

        } catch (Exception e) {
            transfer.setStatus(TransferStatus.FAILED);
            transferRepository.save(transfer);
            log.error("Transfer failed: {}", e.getMessage());
            throw new RuntimeException("Transfer failed: " + e.getMessage());
        }

        return toResponse(transfer);
    }

    public TransferResponse getTransferById(Long id) {
        Transfer transfer = transferRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transfer not found"));
        return toResponse(transfer);
    }

    public List<TransferResponse> getTransfersByIban(String iban) {
        return transferRepository.findBySenderIbanOrReceiverIban(iban, iban)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private TransferResponse transferFallback(TransferRequest request, Throwable t) {
        log.error("Account Service is unavailable: {}", t.getMessage());
        return TransferResponse.builder()
                .senderIban(request.getSenderIban())
                .receiverIban(request.getReceiverIban())
                .amount(request.getAmount())
                .status("FAILED")
                .build();
    }

    private TransferResponse toResponse(Transfer transfer) {
        return TransferResponse.builder()
                .id(transfer.getId())
                .senderIban(transfer.getSenderIban())
                .receiverIban(transfer.getReceiverIban())
                .amount(transfer.getAmount())
                .description(transfer.getDescription())
                .status(transfer.getStatus().name())
                .createdAt(transfer.getCreatedAt())
                .build();
    }
}