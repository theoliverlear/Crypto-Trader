package org.cryptotrader.api.library.services;

import java.util.Date;

public record JwtClaims(String subject, String email, Date issuedAt, Date expiresAt) {
}