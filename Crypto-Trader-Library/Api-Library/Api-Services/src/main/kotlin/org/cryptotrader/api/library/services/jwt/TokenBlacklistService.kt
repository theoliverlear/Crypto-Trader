package org.cryptotrader.api.library.services.jwt

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap

/**
 * Simple in-memory blacklist for JWT tokens to support logout for stateless auth.
 *
 * Purpose:
 * - When a user logs out, we can’t “unsign” a JWT. Instead, we remember its value until it naturally expires and
 *   reject it on subsequent requests.
 *
 * Notes:
 * - Ideal for dev/single-node. In production, use a shared store (Redis) and store a hash of the token.
 */
@Service
class TokenBlacklistService {
    private val logger = LoggerFactory.getLogger(TokenBlacklistService::class.java)

    /** Map of token -> expiry epoch millis. */
    private val blacklistedTokens: MutableMap<String, Long> = ConcurrentHashMap()

    /**
     * Blacklist a token until its expiry time.
     * @param token raw access token string
     * @param expiresAtEpochMillis token expiry (milliseconds since epoch)
     */
    fun blacklistToken(token: String, expiresAtEpochMillis: Long) {
        if (token.isBlank()) return
        // Do not store already expired tokens
        val now = Instant.now().toEpochMilli()
        if (expiresAtEpochMillis <= now) return
        this.blacklistedTokens[token] = expiresAtEpochMillis
        this.logger.debug("Token blacklisted until {}", expiresAtEpochMillis)
    }

    /**
     * Check whether a token is blacklisted (and clean up expired entries opportunistically).
     * @param token raw access token string (nullable)
     * @return true if the token is currently blacklisted
     */
    fun isBlacklisted(token: String?): Boolean {
        if (token.isNullOrBlank()) return false
        val expiry = this.blacklistedTokens[token] ?: return false
        val now = Instant.now().toEpochMilli()
        if (now >= expiry) {
            // cleanup once observed expired
            this.blacklistedTokens.remove(token)
            return false
        }
        return true
    }
}