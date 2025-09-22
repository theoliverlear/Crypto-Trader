package org.cryptotrader.api.library.model.dpop

/**
 * Carries validated DPoP proof context through the filter chain.
 *
 * Fields are descriptive to reduce ambiguity:
 * - keyThumbprint: RFC 7638 JWK thumbprint (aka jkt) for the client public key
 * - proofNonce: Unique per-proof identifier (aka jti) used to prevent replay
 * - httpMethod: The HTTP method covered by the proof (aka htm)
 * - httpUrl: The absolute URL covered by the proof (aka htu)
 * - issuedAtEpochSeconds: Seconds since epoch when the proof was created (aka iat)
 */
 data class DpopProofContext(
    val keyThumbprint: String,
    val proofNonce: String?,
    val httpMethod: String,
    val httpUrl: String,
    val issuedAtEpochSeconds: Long
 )