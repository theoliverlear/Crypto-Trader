package org.cryptotrader.api.library.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import org.cryptotrader.api.library.services.models.JwtClaims
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.util.Assert
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.util.Date

/**
 * Kotlin-native JWT service. Replaces the previous Java JwtService.
 * Phases out session reliance by providing stateless token generation and validation.
 */
@Service
class JwtTokenService(
    @Value($$"${security.jwt.secret:}") secret: String?,
    @param:Value($$"${security.jwt.issuer:crypto-trader-api}") private val issuer: String,
    @param:Value($$"${security.jwt.ttl-seconds:3600}") private val ttlSeconds: Long
) {
    private val algorithm: Algorithm

    init {
        Assert.hasText(secret, "security.jwt.secret must be configured")
        this.algorithm = Algorithm.HMAC256(secret!!.toByteArray(StandardCharsets.UTF_8))
    }

    fun generateToken(subject: String, email: String): String {
        val now = Instant.now()
        val expires = now.plusSeconds(ttlSeconds)
        return JWT.create()
            .withIssuer(issuer)
            .withSubject(subject)
            .withClaim("email", email)
            .withIssuedAt(Date.from(now))
            .withExpiresAt(Date.from(expires))
            .sign(algorithm)
    }

    fun validateAndParse(token: String): JwtClaims {
        val jwt: DecodedJWT = JWT.require(algorithm)
            .withIssuer(issuer)
            .build()
            .verify(token)
        val subject = jwt.subject
        val email = jwt.getClaim("email").asString()
        val issuedAt = jwt.issuedAt
        val expires = jwt.expiresAt
        return JwtClaims(subject, email, issuedAt, expires)
    }

    companion object {
        private val log = LoggerFactory.getLogger(JwtTokenService::class.java)
    }
}
