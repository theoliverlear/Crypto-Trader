package org.cryptotrader.api.library.infrastructure

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.cryptotrader.api.library.entity.user.ProductUser
import org.cryptotrader.api.library.services.JwtService
import org.cryptotrader.api.library.services.ProductUserService
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import kotlin.text.startsWith
import kotlin.text.substringAfter

private val log = KotlinLogging.logger {  }

@Component
class JwtToSessionFilter(
    private val jwtService: JwtService,
    @param:Lazy private val productUserService: ProductUserService
) : OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest,
                                  response: HttpServletResponse,
                                  filterChain: FilterChain) {
        val authHeader = request.getHeader("Authorization")
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val token: String = authHeader.substringAfter("Bearer ")
            try {
                val claims = jwtService.validateAndParse(token)
                val userIdStr = claims.subject
                val email = claims.email
                val userId = userIdStr?.toLongOrNull()
                if (userId != null) {
                    val fetched = productUserService.getUserById(userId)
                    val user = fetched as? ProductUser
                    if (user != null) {
                        if (email == null || email.equals(user.email, ignoreCase = true)) {
                            val session = request.session
                            session.setAttribute("product-user", user)
                            session.setAttribute("user", user)
                        }
                    }
                }
            } catch (ex: Exception) {
                log.error { "Error validating JWT: ${ex.message}" }
            }
        }
        filterChain.doFilter(request, response)
    }
}
