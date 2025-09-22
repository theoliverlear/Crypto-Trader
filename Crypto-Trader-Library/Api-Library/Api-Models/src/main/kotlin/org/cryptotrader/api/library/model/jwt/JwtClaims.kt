package org.cryptotrader.api.library.model.jwt

import java.util.Date

/**
 * Minimal set of claims we extract from a validated access token (JWT).
 *
 * Fields:
 * - subject: the user id (sub)
 * - email: optional email convenience claim (not used for authorization decisions)
 * - issuedAt: time the token was created (iat)
 * - expiresAt: time the token naturally expires (exp)
 * - cnfJkt: optional DPoP confirmation thumbprint (cnf.jkt) when the token is sender-constrained
 */
 data class JwtClaims(
    val subject: String?,
    val email: String?,
    val issuedAt: Date?,
    val expiresAt: Date?,
    val confirmJkt: String? = null
)