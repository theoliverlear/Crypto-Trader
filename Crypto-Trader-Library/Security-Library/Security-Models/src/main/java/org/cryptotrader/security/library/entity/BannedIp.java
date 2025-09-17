package org.cryptotrader.security.library.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.cryptotrader.api.library.entity.Identifiable;

import java.time.LocalDateTime;

@Entity
@Table(name = "banned_ips")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BannedIp extends Identifiable {
    @Column(length = 64)
    private String ip;

    @Column(nullable = false)
    private LocalDateTime occurredAt = LocalDateTime.now();
}
