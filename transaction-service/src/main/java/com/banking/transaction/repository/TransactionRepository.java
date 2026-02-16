package com.banking.transaction.repository;

import com.banking.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySenderIbanOrReceiverIban(String senderIban, String receiverIban);
    List<Transaction> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    List<Transaction> findBySenderIbanAndCreatedAtBetween(String iban, LocalDateTime start, LocalDateTime end);
}