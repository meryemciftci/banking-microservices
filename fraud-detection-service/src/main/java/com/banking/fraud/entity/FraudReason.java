package com.banking.fraud.entity;

public enum FraudReason {
    DAILY_LIMIT_EXCEEDED,
    TOO_MANY_TRANSFERS,
    BLACKLISTED_IBAN,
    NIGHT_HIGH_AMOUNT,
    SUSPICIOUS_LOCATION
}