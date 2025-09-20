package org.cryptotrader.api.library.infrastructure

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.cryptotrader.api.library.entity.user.ProductUser
import org.cryptotrader.api.library.services.models.JwtClaims
import org.cryptotrader.api.library.services.JwtTokenService
import org.cryptotrader.api.library.services.ProductUserService
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtTokenService: JwtTokenService,
    private val productUserService: ProductUserService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val token = extractBearerToken(request)
            if (!token.isNullOrBlank()) {
                val claims = jwtTokenService.validateAndParse(token)
                val user = resolveUserFromClaims(claims)
                if (user != null && emailsMatch(claims.email, user.email)) {
                    authenticate(user, request)
                } else {
                    SecurityContextHolder.clearContext()
                }
            } else {
                SecurityContextHolder.clearContext()
            }
        } catch (ex: Exception) {
            SecurityContextHolder.clearContext()
            log.debug("JWT authentication failed; proceeding unauthenticated", ex)
        }
        filterChain.doFilter(request, response)
    }

    private fun extractBearerToken(request: HttpServletRequest): String? =
        request.getHeader("Authorization")?.takeIf { it.startsWith("Bearer ") }?.substringAfter("Bearer ")

    private fun resolveUserFromClaims(claims: JwtClaims): ProductUser? {
        val userId = claims.subject?.toLongOrNull() ?: return null
        val fetched = productUserService.getUserById(userId)
        return fetched as? ProductUser
    }

    private fun emailsMatch(emailClaim: String?, userEmail: String?): Boolean =
        emailClaim == null || (userEmail != null && emailClaim.equals(userEmail, ignoreCase = true))

    private fun authenticate(user: ProductUser, request: HttpServletRequest) {
        val authentication = UsernamePasswordAuthenticationToken(user, null, emptyList())
        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authentication
    }

    companion object {
        private val log = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)
    }
}
