package org.cryptotrader.api.library.infrastructure

import org.slf4j.LoggerFactory
import jakarta.servlet.http.HttpServletRequest
import org.cryptotrader.api.library.entity.user.ProductUser
import org.cryptotrader.api.library.infrastructure.extension.getBearerToken
import org.cryptotrader.api.library.infrastructure.extension.isBearer
import org.cryptotrader.api.library.model.jwt.JwtClaims
import org.cryptotrader.api.library.services.jwt.JwtTokenService
import org.cryptotrader.api.library.services.ProductUserService
import org.cryptotrader.api.library.services.jwt.TokenBlacklistService
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

private val log = LoggerFactory.getLogger(JwtHandshakeInterceptor::class.java)

/**
 * WebSocket handshake interceptor that optionally authenticates the upgrading connection using a JWT.
 *
 * How it works:
 * - Looks for an access token in either the Authorization: Bearer header or token query parameter on the WS URL.
 * - If present and not blacklisted, validates it and attaches the ProductUser to the WebSocket attributes.
 * - Does not enforce authentication; handlers may still allow anonymous connections and authorize per-message.
 *
 * Relation to DPoP:
 * - DPoP applies to HTTP requests, not WebSocket frames. We allow tokens issued via HTTP (DPoP-bound) to be
 *   presented at upgrade using the Bearer scheme. Binding is enforced on HTTP APIs; WS connections should close
 *   or require re-auth when the token expires.
 */
@Component
class JwtHandshakeInterceptor(
    private val jwtService: JwtTokenService,
    private val productUserService: ProductUserService,
    private val tokenBlacklistService: TokenBlacklistService
) : HandshakeInterceptor {

    override fun beforeHandshake(request: ServerHttpRequest,
                                 response: ServerHttpResponse,
                                 wsHandler: WebSocketHandler,
                                 attributes: MutableMap<String, Any>): Boolean {
        log.info("Handshake: {}", request.uri)
        try {
            val servletRequest: HttpServletRequest? = this.getServletRequest(request)
            val token: String? = this.chooseToken(this.getAuthHeader(request), this.getQueryToken(servletRequest))
            if (!token.isNullOrBlank()) {
                if (!tokenBlacklistService.isBlacklisted(token)) {
                    val user: ProductUser? = this.validateAndFetchUser(token)
                    if (user != null) {
                        this.attachUser(attributes, user)
                    }
                } else {
                    log.debug("JWT handshake token is blacklisted; skipping user attach")
                }
            }
        } catch (ex: Exception) {
            // Invalid or missing token: proceed without authentication; handlers may enforce auth as needed
            log.debug("JWT handshake interceptor: proceeding without authenticated user", ex)
        }
        return true
    }

    private fun chooseToken(authHeader: String?, queryToken: String?): String? = when {
        this.isBearerToken(authHeader) -> authHeader.getBearerToken()
        !queryToken.isNullOrBlank() -> queryToken
        else -> null
    }

    private fun validateAndFetchUser(token: String): ProductUser? {
        val claims: JwtClaims = this.jwtService.validateAndParse(token)
        val userId: Long = claims.subject?.toLongOrNull() ?: return null
        val user: ProductUser? = this.productUserService.getUserById(userId)
        return if (this.isUserAuthenticated(user, claims)) {
            user
        } else null
    }

    private fun isUserAuthenticated(user: ProductUser?,
                                    claims: JwtClaims): Boolean = 
        user != null && emailsMatch(claims.email, user.email)

    private fun attachUser(attributes: MutableMap<String, Any>, user: ProductUser) {
        // Attach only to WebSocket attributes; do not touch HTTP session (stateless JWT)
        attributes["product-user"] = user
    }

    private fun emailsMatch(emailClaim: String?, userEmail: String?): Boolean =
        emailClaim == null || (userEmail != null && emailClaim.equals(userEmail, ignoreCase = true))

    @OptIn(ExperimentalContracts::class)
    private fun isBearerToken(authHeader: String?): Boolean {
        contract {
            returns(true) implies (authHeader != null)
        }
        return authHeader != null && authHeader.isBearer()
    }

    private fun getQueryToken(servletRequest: HttpServletRequest?): String? =
        servletRequest?.getParameter("token")

    private fun getAuthHeader(request: ServerHttpRequest): String? =
        request.headers.getFirst("Authorization")

    private fun getServletRequest(request: ServerHttpRequest): HttpServletRequest? =
        (request as? ServletServerHttpRequest)?.servletRequest

    override fun afterHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        exception: Exception?
    ) {
        // no-op
    }
}
