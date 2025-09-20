package org.cryptotrader.api.library.services.models

import java.util.Date

data class JwtClaims(
    val subject: String?,
    val email: String?,
    val issuedAt: Date?,
    val expiresAt: Date?
)