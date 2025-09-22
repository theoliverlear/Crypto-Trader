package org.cryptotrader.api.library.scripts.http

import org.springframework.http.ResponseCookie
import java.time.Duration
import java.time.Instant

/**
 * Helper for building and clearing HttpOnly refresh cookies in a consistent, secure way.
 *
 * Defaults:
 * - HttpOnly=true, Path=/ (cookie not readable by JS and applies site-wide)
 * - SameSite=Strict for the default overload (better CSRF posture when SPA/API are same-site)
 * - Secure=true for the default overload (required in production; override via explicit-overload for http localhost)
 *
 * __Host- prefix:
 * - When using a cookie name that starts with "__Host-", browsers require Secure, Path=/ and no Domain attribute.
 *   This is the recommended setup for production single-domain deployments.
 */
object CookieScript {
    /**
     * Build a production-grade refresh cookie with secure defaults.
     *
     * This overload always sets Secure=true and SameSite=Strict, and Path=/.
     *
     * @param name cookie name (e.g., __Host-rt)
     * @param value opaque token id value
     * @param expiresAt absolute expiry used to compute Max-Age
     * @return a ResponseCookie ready to be added to Set-Cookie
     */
    fun buildRefreshCookie(name: String,
                           value: String,
                           expiresAt: Instant): ResponseCookie {
        val maxAge: Long = this.getMaxAge(expiresAt)
        return ResponseCookie.from(name, value)
            .httpOnly(true)
            .secure(true)
            .sameSite("Strict")
            .path("/")
            .maxAge(maxAge)
            .build()
    }

    /**
     * Build a refresh cookie with environment-specific flags (e.g., dev over http).
     *
     * Use this overload to disable Secure or to set SameSite=None when running over HTTPS with cross-site SPA.
     *
     * @param name cookie name
     * @param value opaque token id
     * @param expiresAt expiry instant
     * @param secure whether to mark Secure
     * @param sameSite SameSite policy (Strict/Lax/None)
     * @return a ResponseCookie ready to be added to Set-Cookie
     */
    fun buildRefreshCookie(name: String, 
                           value: String,
                           expiresAt: Instant,
                           secure: Boolean,
                           sameSite: String): ResponseCookie {
        val maxAge: Long = this.getMaxAge(expiresAt)
        return ResponseCookie.from(name, value)
            .httpOnly(true)
            .secure(secure)
            .sameSite(sameSite)
            .path("/")
            .maxAge(maxAge)
            .build()
    }

    private fun getMaxAge(expiresAt: Instant): Long =
        Duration.between(Instant.now(), expiresAt).seconds.coerceAtLeast(0)

    /**
     * Create a deletion cookie (Max-Age=0) using secure defaults.
     *
     * @param name cookie name to clear
     * @return a ResponseCookie that instructs the browser to delete the cookie
     */
    fun deleteCookie(name: String): ResponseCookie =
        ResponseCookie.from(name, "")
            .httpOnly(true)
            .secure(true)
            .sameSite("Strict")
            .path("/")
            .maxAge(0)
            .build()

    /**
     * Create a deletion cookie (Max-Age=0) with environment-specific flags.
     *
     * @param name cookie name to clear
     * @param secure whether to mark Secure
     * @param sameSite SameSite policy (Strict/Lax/None)
     * @return a ResponseCookie that instructs the browser to delete the cookie
     */
    fun deleteCookie(name: String, 
                     secure: Boolean,
                     sameSite: String): ResponseCookie =
        ResponseCookie.from(name, "")
            .httpOnly(true)
            .secure(secure)
            .sameSite(sameSite)
            .path("/")
            .maxAge(0)
            .build()
}