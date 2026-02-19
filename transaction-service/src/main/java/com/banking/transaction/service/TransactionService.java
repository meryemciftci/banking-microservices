package com.banking.transaction.service;

import com.banking.transaction.dto.event.TransferEvent;
import com.banking.transaction.entity.Transaction;
import com.banking.transaction.entity.TransactionType;
import com.banking.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @RabbitListener(queues = "transaction-queue")
    public void handleTransferEvent(TransferEvent event) {
        log.info("Received transfer event: {}", event.getTransferId());

        Transaction transaction = Transaction.builder()
                .transferId(event.getTransferId())
                .senderIban(event.getSenderIban())
                .receiverIban(event.getReceiverIban())
                .amount(event.getAmount())
                .description(event.getDescription())
                .type(TransactionType.TRANSFER)
                .createdAt(event.getCreatedAt())
                .build();

        transactionRepository.save(transaction);
        log.info("Transaction saved for transfer: {}", event.getTransferId());
    }

    public List<Transaction> getTransactionsByIban(String iban) {
        return transactionRepository.findBySenderIbanOrReceiverIban(iban, iban);
    }

    public List<Transaction> getTransactionsByDateRange(LocalDateTime start, LocalDateTime end) {
        return transactionRepository.findByCreatedAtBetween(start, end);
    }
}