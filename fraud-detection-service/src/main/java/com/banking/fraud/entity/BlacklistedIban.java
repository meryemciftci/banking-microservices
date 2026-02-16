package com.banking.fraud.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "blacklisted_ibans")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlacklistedIban {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String iban;

    private String reason;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}