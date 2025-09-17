package org.cryptotrader.security.library.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.cryptotrader.api.library.entity.Identifiable;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public class IpAddress extends Identifiable {
    @Column(length = 64, name = "ip_address", nullable = false)
    protected String ipAddress;
}
