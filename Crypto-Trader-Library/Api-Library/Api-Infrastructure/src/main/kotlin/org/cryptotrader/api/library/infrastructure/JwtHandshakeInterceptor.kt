package org.cryptotrader.api.library.infrastructure

import io.github.oshai.kotlinlogging.KotlinLogging
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

private val log = KotlinLogging.logger {}

@Component
class JwtHandshakeInterceptor(
    private val jwtService: JwtService,
    private val productUserService: ProductUserService
) : HandshakeInterceptor {

    override fun beforeHandshake(request: ServerHttpRequest,
                                 response: ServerHttpResponse,
                                 wsHandler: WebSocketHandler,
                                 attributes: MutableMap<String, Any>): Boolean {
        try {
            val servletRequest: HttpServletRequest? = (request as? ServletServerHttpRequest)?.servletRequest
            val authHeader: String? = request.headers.getFirst("Authorization")
            val queryToken: String? = servletRequest?.getParameter("token")
            val token: String? = when {
                authHeader != null && authHeader.startsWith("Bearer ") -> authHeader.substringAfter("Bearer ")
                !queryToken.isNullOrBlank() -> queryToken
                else -> null
            }
            if (!token.isNullOrBlank()) {
                val claims: JwtClaims? = jwtService.validateAndParse(token)
                val userIdStr = claims?.subject
                val email = claims?.email
                val userId = userIdStr?.toLongOrNull()
                if (userId != null) {
                    val fetched = productUserService.getUserById(userId)
                    val user = fetched as? ProductUser
                    if (user != null) {
                        if (email == null || email.equals(user.email, ignoreCase = true)) {
                            val httpSession = servletRequest?.session
                            httpSession?.setAttribute("product-user", user)
                            httpSession?.setAttribute("user", user)
                            attributes["product-user"] = user
                            attributes["user"] = user
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            // Invalid or missing token: proceed without authentication; handlers may enforce auth as needed
            log.debug(ex) { "JWT handshake interceptor: proceeding without authenticated user" }
        }
        return true
    }

    override fun afterHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        exception: Exception?
    ) {
        // no-op
    }
}
