package com.banking.account.service;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class IbanGenerator {

    private static final String COUNTRY_CODE = "TR";
    private static final String BANK_CODE = "00061";
    private static final Random random = new Random();

    public String generateIban() {
        StringBuilder sb = new StringBuilder();
        sb.append(COUNTRY_CODE);
        sb.append(String.format("%02d", random.nextInt(100)));
        sb.append(BANK_CODE);
        for (int i = 0; i < 16; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}