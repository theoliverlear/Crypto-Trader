package org.cryptotrader.security.library.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "banned_ips")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BannedIpAddress extends IpAddress {

    @Column(nullable = false, name = "occurred_at")
    private LocalDateTime occurredAt = LocalDateTime.now();
}
