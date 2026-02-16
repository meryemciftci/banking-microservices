package com.banking.fraud.repository;

import com.banking.fraud.entity.FraudAlert;
import com.banking.fraud.entity.FraudStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FraudAlertRepository extends JpaRepository<FraudAlert, Long> {
    List<FraudAlert> findByStatus(FraudStatus status);
    List<FraudAlert> findBySenderIban(String senderIban);
}