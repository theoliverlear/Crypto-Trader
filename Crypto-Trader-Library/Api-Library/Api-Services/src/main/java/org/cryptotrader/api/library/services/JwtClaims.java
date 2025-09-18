package org.cryptotrader.api.library.services;

import java.util.Date;

public class JwtClaims {
    private final String subject;
    private final String email;
    private final Date issuedAt;
    private final Date expiresAt;

    public JwtClaims(String subject,
                     String email,
                     Date issuedAt,
                     Date expiresAt) {
        this.subject = subject;
        this.email = email;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
    }

    public String getSubject() { return subject; }
    public String getEmail() { return email; }
    public Date getIssuedAt() { return issuedAt; }
    public Date getExpiresAt() { return expiresAt; }
}