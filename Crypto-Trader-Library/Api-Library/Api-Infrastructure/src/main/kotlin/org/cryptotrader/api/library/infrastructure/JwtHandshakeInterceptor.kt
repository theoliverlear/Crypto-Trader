package org.cryptotrader.api.library.infrastructure

import org.slf4j.LoggerFactory
import jakarta.servlet.http.HttpServletRequest
import org.cryptotrader.api.library.entity.user.ProductUser
import org.cryptotrader.api.library.services.JwtClaims
import org.cryptotrader.api.library.services.JwtTokenService
import org.cryptotrader.api.library.services.ProductUserService
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

private val log = LoggerFactory.getLogger(JwtHandshakeInterceptor::class.java)

@Component
class JwtHandshakeInterceptor(
    private val jwtService: JwtTokenService,
    private val productUserService: ProductUserService
) : HandshakeInterceptor {

    override fun beforeHandshake(request: ServerHttpRequest,
                                 response: ServerHttpResponse,
                                 wsHandler: WebSocketHandler,
                                 attributes: MutableMap<String, Any>): Boolean {
        log.info("Handshake: {}", request.uri)
        try {
            val servletRequest = this.getServletRequest(request)
            val token = this.chooseToken(this.getAuthHeader(request), this.getQueryToken(servletRequest))
            if (!token.isNullOrBlank()) {
                val user = this.validateAndFetchUser(token)
                if (user != null) {
                    this.attachUser(attributes, user)
                }
            }
        } catch (ex: Exception) {
            // Invalid or missing token: proceed without authentication; handlers may enforce auth as needed
            log.debug("JWT handshake interceptor: proceeding without authenticated user", ex)
        }
        return true
    }

    private fun chooseToken(authHeader: String?, queryToken: String?): String? = when {
        this.isBearerToken(authHeader) -> authHeader!!.substringAfter("Bearer ")
        !queryToken.isNullOrBlank() -> queryToken
        else -> null
    }

    private fun validateAndFetchUser(token: String): ProductUser? {
        val claims: JwtClaims = this.jwtService.validateAndParse(token)
        val userId = claims.subject?.toLongOrNull() ?: return null
        val fetched = this.productUserService.getUserById(userId)
        val user = fetched as? ProductUser
        return if (user != null && emailsMatch(claims.email, user.email)) user else null
    }

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
        return authHeader != null && authHeader.startsWith("Bearer ")
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
