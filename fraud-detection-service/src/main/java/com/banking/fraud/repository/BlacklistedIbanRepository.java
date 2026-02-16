package com.banking.fraud.repository;

import com.banking.fraud.entity.BlacklistedIban;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistedIbanRepository extends JpaRepository<BlacklistedIban, Long> {
    Boolean existsByIban(String iban);
}