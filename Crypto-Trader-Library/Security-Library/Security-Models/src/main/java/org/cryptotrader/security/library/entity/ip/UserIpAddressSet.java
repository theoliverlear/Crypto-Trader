package org.cryptotrader.security.library.entity.ip;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.cryptotrader.api.library.entity.Identifiable;
import org.cryptotrader.api.library.entity.user.ProductUser;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "user_ip_address_sets")
@NoArgsConstructor
@AllArgsConstructor
public class UserIpAddressSet extends Identifiable {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private ProductUser user;

    @OneToMany(mappedBy = "userIpAddressSet", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserIpAddress> ipAddresses;
}
