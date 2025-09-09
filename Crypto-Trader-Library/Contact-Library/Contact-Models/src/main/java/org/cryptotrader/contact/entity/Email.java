package org.cryptotrader.contact.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.cryptotrader.entity.Identifiable;

@Entity
@Getter
@Setter
@Table(name = "emails")
// TODO: Build dedicated builder model for robust handling.
@Builder
@AllArgsConstructor
public class Email extends Identifiable {
    
    @Enumerated(EnumType.STRING)
    private CryptoTraderMailer cryptoTraderMailer;
    @Column(name = "to_address", nullable = false)
    private String toAddress;
    @Column(name = "subject", nullable = false)
    private String subject;
    @Column(name = "body", nullable = false)
    private String body;
    
    @Enumerated(EnumType.STRING)
    private EmailType type;
    
    public Email() {
        super();
        this.type = EmailType.OTHER;
    }
}


