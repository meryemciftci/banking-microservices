package com.banking.transfer.repository;

import com.banking.transfer.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
    List<Transfer> findBySenderIbanOrReceiverIban(String senderIban, String receiverIban);
}