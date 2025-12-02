package org.cryptotrader.api.library.infrastructure

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.cryptotrader.api.library.entity.user.ProductUser
import org.cryptotrader.api.library.infrastructure.extension.getAuthHeader
import org.cryptotrader.api.library.infrastructure.extension.getBearerToken
import org.cryptotrader.api.library.infrastructure.extension.getDpopToken
import org.cryptotrader.api.library.infrastructure.extension.isBearer
import org.cryptotrader.api.library.infrastructure.extension.isDpop
import org.cryptotrader.api.library.model.jwt.JwtClaims
import org.cryptotrader.api.library.services.jwt.JwtTokenService
import org.cryptotrader.api.library.services.ProductUserService
import org.cryptotrader.api.library.services.jwt.TokenBlacklistService
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * Purpose: Turn a valid access token into a logged-in user for this request.
 *
 * In plain words:
 * - We look for an access token in the Authorization header.
 * - If the token is valid and not blacklisted, we look up the user and mark the request as authenticated.
 * - This does not do DPoP checks; a separate filter verifies the DPoP proof when the token is DPoP-bound.
 *
 * Accepted Authorization schemes:
 * - "Bearer <jwt>" — legacy or unbound tokens.
 * - "DPoP <jwt>" — tokens that are tied to a browser key (a DPoP header must also be present; checked earlier).
 *
 * Result: On success, the current user is placed into Spring Security’s context so controllers can inject it.
 */
@Component
class JwtAuthenticationFilter(
    private val jwtTokenService: JwtTokenService,
    private val productUserService: ProductUserService,
    private val tokenBlacklistService: TokenBlacklistService
) : OncePerRequestFilter() {
    companion object {
        private val log = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)
    }

    override fun doFilterInternal(request: HttpServletRequest,
                                  response: HttpServletResponse,
                                  filterChain: FilterChain) {
        try {
            val accessToken: String? = this.extractAuthorizationToken(request)
            if (!accessToken.isNullOrBlank()) {
                this.handleTokenAuthentication(accessToken, request)
            } else {
                SecurityContextHolder.clearContext()
            }
        } catch (ex: Exception) {
            SecurityContextHolder.clearContext()
            log.debug("JWT authentication failed; proceeding unauthenticated", ex)
        }
        filterChain.doFilter(request, response)
    }

    private fun handleTokenAuthentication(accessToken: String,
                                          request: HttpServletRequest) {
        if (this.tokenBlacklistService.isBlacklisted(accessToken)) {
            SecurityContextHolder.clearContext()
        } else {
            val jwtClaims: JwtClaims = this.jwtTokenService.validateAndParse(accessToken)
            val productUser: ProductUser? = this.resolveUserFromClaims(jwtClaims)
            if (isValidUser(productUser, jwtClaims)) {
                this.authenticate(productUser, request)
            } else {
                SecurityContextHolder.clearContext()
            }
        }
    }

    @OptIn(ExperimentalContracts::class)
    private fun isValidUser(productUser: ProductUser?,
                            jwtClaims: JwtClaims): Boolean {
        contract {
            returns(true) implies (productUser != null)
        }
        return productUser != null && emailsMatch(jwtClaims.email, productUser.email)
    }

    private fun extractAuthorizationToken(request: HttpServletRequest): String? =
        request.getAuthHeader()?.let {
            when {
                it.isBearer() -> it.getBearerToken()
                it.isDpop() -> it.getDpopToken()
                else -> null
            }
        }

    private fun resolveUserFromClaims(jwtClaims: JwtClaims): ProductUser? {
        val userId: Long = jwtClaims.subject?.toLongOrNull() ?: return null
        val fetchedUser: ProductUser? = this.productUserService.getUserById(userId)
        return fetchedUser
    }

    private fun emailsMatch(emailClaim: String?, userEmail: String?): Boolean =
        emailClaim == null || (userEmail != null && emailClaim.equals(userEmail, ignoreCase = true))

    private fun authenticate(productUser: ProductUser, request: HttpServletRequest) {
        val authentication = UsernamePasswordAuthenticationToken(
            productUser,
            null,
            productUser.authorities
        )
        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authentication
        log.debug("Authenticated user ({}): {}", productUser.id, productUser.email)
    }
}
