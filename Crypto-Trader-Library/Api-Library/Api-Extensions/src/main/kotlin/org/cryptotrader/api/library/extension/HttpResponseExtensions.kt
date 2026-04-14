package org.cryptotrader.api.library.extension

import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import jakarta.servlet.http.HttpServletResponse

/**
 * HTTP response extensions for writing consistent error responses.
 */

/**
 * Write a 401 Unauthorized response with a compact JSON body and optional WWW-Authenticate header.
 *
 * Body shape:
 *   {"code":"<machine_code>","reason":"<human_readable_reason>"}
 *
 * @param code short machine-friendly string (e.g., "invalid_dpop", "cnf_mismatch")
 * @param reason human-readable explanation
 * @param wwwAuthenticate optional value for the WWW-Authenticate header (e.g., "DPoP")
 */
fun HttpServletResponse.sendUnauthorized(code: String,
                                         reason: String,
                                         wwwAuthenticate: String? = null) {
    SecurityContextHolder.clearContext()
    this.status = HttpStatus.UNAUTHORIZED.value()
    if (!wwwAuthenticate.isNullOrBlank()) {
        this.setHeader("WWW-Authenticate", wwwAuthenticate)
    }
    this.contentType = "application/json"
    val body = """{"code":"$code","reason":"$reason"}"""
    this.writer.use { it.write(body) }
}