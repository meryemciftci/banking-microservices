package com.banking.fraud.service;

import com.banking.fraud.entity.BlacklistedIban;
import com.banking.fraud.entity.FraudAlert;
import com.banking.fraud.entity.FraudReason;
import com.banking.fraud.entity.FraudStatus;
import com.banking.fraud.repository.BlacklistedIbanRepository;
import com.banking.fraud.repository.FraudAlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FraudDetectionService {

    private final FraudAlertRepository fraudAlertRepository;
    private final BlacklistedIbanRepository blacklistedIbanRepository;
    private final RedisTemplate<String, String> redisTemplate;

    private static final BigDecimal DAILY_LIMIT = new BigDecimal("50000");
    private static final int MAX_TRANSFERS_PER_HOUR = 5;

    public boolean checkFraud(Long transferId, String senderIban, String receiverIban, BigDecimal amount) {
        // 1. Blacklist check
        if (blacklistedIbanRepository.existsByIban(receiverIban)) {
            saveFraudAlert(transferId, senderIban, receiverIban, amount, FraudReason.BLACKLISTED_IBAN);
            log.warn("FRAUD: Blacklisted IBAN detected - {}", receiverIban);
            return true;
        }

        // 2. Daily limit check (Redis)
        String dailyKey = "daily_transfer:" + senderIban;
        String dailyTotal = redisTemplate.opsForValue().get(dailyKey);
        BigDecimal currentTotal = dailyTotal != null ? new BigDecimal(dailyTotal) : BigDecimal.ZERO;

        if (currentTotal.add(amount).compareTo(DAILY_LIMIT) > 0) {
            saveFraudAlert(transferId, senderIban, receiverIban, amount, FraudReason.DAILY_LIMIT_EXCEEDED);
            log.warn("FRAUD: Daily limit exceeded for IBAN - {}", senderIban);
            return true;
        }

        // 3. Too many transfers per hour (Redis)
        String hourlyKey = "hourly_count:" + senderIban;
        String hourlyCount = redisTemplate.opsForValue().get(hourlyKey);
        int count = hourlyCount != null ? Integer.parseInt(hourlyCount) : 0;

        if (count >= MAX_TRANSFERS_PER_HOUR) {
            saveFraudAlert(transferId, senderIban, receiverIban, amount, FraudReason.TOO_MANY_TRANSFERS);
            log.warn("FRAUD: Too many transfers for IBAN - {}", senderIban);
            return true;
        }

        // 4. Night high amount check (02:00 - 06:00)
        int hour = LocalDateTime.now().getHour();
        if (hour >= 2 && hour <= 6 && amount.compareTo(new BigDecimal("10000")) > 0) {
            saveFraudAlert(transferId, senderIban, receiverIban, amount, FraudReason.NIGHT_HIGH_AMOUNT);
            log.warn("FRAUD: Night high amount transfer - {}", senderIban);
            return true;
        }

        // Update Redis counters
        updateDailyTotal(dailyKey, currentTotal.add(amount));
        updateHourlyCount(hourlyKey, count + 1);

        return false;
    }

    private void updateDailyTotal(String key, BigDecimal total) {
        redisTemplate.opsForValue().set(key, total.toString(), Duration.ofHours(24));
    }

    private void updateHourlyCount(String key, int count) {
        redisTemplate.opsForValue().set(key, String.valueOf(count), Duration.ofHours(1));
    }

    private void saveFraudAlert(Long transferId, String senderIban, String receiverIban, BigDecimal amount, FraudReason reason) {
        FraudAlert alert = FraudAlert.builder()
                .transferId(transferId)
                .senderIban(senderIban)
                .receiverIban(receiverIban)
                .amount(amount)
                .reason(reason)
                .status(FraudStatus.PENDING)
                .build();
        fraudAlertRepository.save(alert);
    }

    public List<FraudAlert> getAllAlerts() {
        return fraudAlertRepository.findAll();
    }

    public List<FraudAlert> getPendingAlerts() {
        return fraudAlertRepository.findByStatus(FraudStatus.PENDING);
    }

    public void addToBlacklist(String iban, String reason) {
        if (blacklistedIbanRepository.existsByIban(iban)) {
            throw new RuntimeException("IBAN already blacklisted");
        }
        BlacklistedIban blacklisted = BlacklistedIban.builder()
                .iban(iban)
                .reason(reason)
                .build();
        blacklistedIbanRepository.save(blacklisted);
    }

    public void removeFromBlacklist(String iban) {
        blacklistedIbanRepository.deleteById(
                blacklistedIbanRepository.findAll().stream()
                        .filter(b -> b.getIban().equals(iban))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("IBAN not found in blacklist"))
                        .getId()
        );
    }
}