package org.cryptotrader.api.library.infrastructure

import org.slf4j.LoggerFactory
import jakarta.servlet.http.HttpServletRequest
import org.cryptotrader.api.library.entity.user.ProductUser
import org.cryptotrader.api.library.services.JwtClaims
import org.cryptotrader.api.library.services.JwtService
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
    private val jwtService: JwtService,
    private val productUserService: ProductUserService
) : HandshakeInterceptor {

    override fun beforeHandshake(request: ServerHttpRequest,
                                 response: ServerHttpResponse,
                                 wsHandler: WebSocketHandler,
                                 attributes: MutableMap<String, Any>): Boolean {
        log.info("Handshake: {}", request.uri)
        try {
            val servletRequest: HttpServletRequest? = this.getServletRequest(request)
            val authHeader: String? = this.getAuthHeader(request)
            val queryToken: String? = this.getQueryToken(servletRequest)
            val token: String? = when {
                this.isBearerToken(authHeader) -> authHeader.substringAfter("Bearer ")
                !queryToken.isNullOrBlank() -> queryToken
                else -> null
            }
            if (!token.isNullOrBlank()) {
                val claims: JwtClaims? = this.jwtService.validateAndParse(token)
                val userIdStr = claims?.subject
                val email = claims?.email
                val userId = userIdStr?.toLongOrNull()
                if (userId != null) {
                    val fetched = this.productUserService.getUserById(userId)
                    val user = fetched as? ProductUser
                    if (user != null) {
                        if (email == null || email.equals(user.email, ignoreCase = true)) {
                            val httpSession = servletRequest?.session
                            httpSession?.setAttribute("product-user", user)
                            attributes["product-user"] = user
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            // Invalid or missing token: proceed without authentication; handlers may enforce auth as needed
            log.debug("JWT handshake interceptor: proceeding without authenticated user", ex)
        }
        return true
    }

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
