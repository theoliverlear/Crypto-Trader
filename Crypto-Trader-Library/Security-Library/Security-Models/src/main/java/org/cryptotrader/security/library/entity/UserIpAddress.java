package org.cryptotrader.security.library.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_ip_addresses")
@NoArgsConstructor
@AllArgsConstructor
public class UserIpAddress extends IpAddress {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_ip_address_set_id", nullable = false)
    private UserIpAddressSet userIpAddressSet;
}
