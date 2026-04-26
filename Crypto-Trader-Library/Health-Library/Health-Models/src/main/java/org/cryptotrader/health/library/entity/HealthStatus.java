package org.cryptotrader.health.library.entity;

import jakarta.persistence.*;
import lombok.*;
import org.cryptotrader.health.library.model.CryptoTraderService;
import org.cryptotrader.health.library.model.ServiceStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "health_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service")
    @Enumerated(EnumType.STRING)
    private CryptoTraderService service;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ServiceStatus status;

    @Column(name = "http_status_code")
    private int httpStatusCode;

    @Column(columnDefinition = "TEXT")
    private String details;

    @Column(name = "checked_at")
    private LocalDateTime checkedAt;
}
