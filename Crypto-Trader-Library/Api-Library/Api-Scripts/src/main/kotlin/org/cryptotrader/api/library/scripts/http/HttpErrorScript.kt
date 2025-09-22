package org.cryptotrader.api.library.scripts.http

import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import jakarta.servlet.http.HttpServletResponse

/**
 * Small helpers for writing consistent HTTP error responses.
 *
 * Design:
 * - Stateless utilities that do not depend on Spring MVC, suitable for use from filters.
 * - Responses are minimal JSON objects with a machine-friendly "code" and a human "reason".
 */
object HttpErrorScript {
    /**
     * Write a 401 Unauthorized response with a compact JSON body and optional WWW-Authenticate header.
     *
     * Body shape:
     *   {"code":"<machine_code>","reason":"<human_readable_reason>"}
     *
     * @param response HttpServletResponse to write to (SecurityContext is cleared)
     * @param code short machine-friendly string (e.g., "invalid_dpop", "cnf_mismatch")
     * @param reason human-readable explanation
     * @param wwwAuthenticate optional value for the WWW-Authenticate header (e.g., "DPoP")
     */
    @JvmStatic
    fun unauthorized(response: HttpServletResponse,
                     code: String,
                     reason: String,
                     wwwAuthenticate: String? = null) {
        SecurityContextHolder.clearContext()
        response.status = HttpStatus.UNAUTHORIZED.value()
        if (!wwwAuthenticate.isNullOrBlank()) {
            response.setHeader("WWW-Authenticate", wwwAuthenticate)
        }
        response.contentType = "application/json"
        val body = """{"code":"$code","reason":"$reason"}"""
        response.writer.use { it.write(body) }
    }
}