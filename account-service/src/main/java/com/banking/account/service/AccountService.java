package com.banking.account.service;

import com.banking.account.dto.request.CreateAccountRequest;
import com.banking.account.dto.request.UpdateStatusRequest;
import com.banking.account.dto.response.AccountResponse;
import com.banking.account.entity.Account;
import com.banking.account.entity.AccountStatus;
import com.banking.account.entity.AccountType;
import com.banking.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final IbanGenerator ibanGenerator;

    @Transactional
    public AccountResponse createAccount(CreateAccountRequest request) {
        String iban;
        do {
            iban = ibanGenerator.generateIban();
        } while (accountRepository.existsByIban(iban));

        AccountType type = AccountType.CHECKING;
        if (request.getAccountType() != null) {
            type = AccountType.valueOf(request.getAccountType().toUpperCase());
        }

        Account account = Account.builder()
                .iban(iban)
                .userId(request.getUserId())
                .accountHolderName(request.getAccountHolderName())
                .accountType(type)
                .balance(BigDecimal.ZERO)
                .status(AccountStatus.ACTIVE)
                .build();

        accountRepository.save(account);

        return toResponse(account);
    }

    public AccountResponse getAccountByIban(String iban) {
        Account account = accountRepository.findByIban(iban)
                .orElseThrow(() -> new RuntimeException("Account not found with IBAN: " + iban));
        return toResponse(account);
    }

    public List<AccountResponse> getAccountsByUserId(Long userId) {
        return accountRepository.findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public AccountResponse updateStatus(String iban, UpdateStatusRequest request) {
        Account account = accountRepository.findByIban(iban)
                .orElseThrow(() -> new RuntimeException("Account not found with IBAN: " + iban));

        account.setStatus(AccountStatus.valueOf(request.getStatus().toUpperCase()));
        account.setUpdatedAt(LocalDateTime.now());
        accountRepository.save(account);

        return toResponse(account);
    }

    @Transactional
    public void updateBalance(String iban, BigDecimal amount) {
        Account account = accountRepository.findByIban(iban)
                .orElseThrow(() -> new RuntimeException("Account not found with IBAN: " + iban));

        BigDecimal newBalance = account.getBalance().add(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(newBalance);
        account.setUpdatedAt(LocalDateTime.now());
        accountRepository.save(account);
    }

    private AccountResponse toResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .iban(account.getIban())
                .userId(account.getUserId())
                .accountHolderName(account.getAccountHolderName())
                .accountType(account.getAccountType().name())
                .balance(account.getBalance())
                .status(account.getStatus().name())
                .createdAt(account.getCreatedAt())
                .build();
    }
}