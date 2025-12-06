package org.cryptotrader.console.library.component

import org.cryptotrader.api.library.entity.user.ProductUser
import org.cryptotrader.api.library.model.jwt.JwtClaims
import org.cryptotrader.api.library.services.ProductUserService
import org.cryptotrader.api.library.services.jwt.TokenBlacklistService
import org.cryptotrader.console.library.component.models.AccessTokenVerifier
import org.cryptotrader.universal.library.extension.string.getBearerToken
import org.cryptotrader.universal.library.extension.string.getDpopToken
import org.cryptotrader.universal.library.extension.string.isBearer
import org.cryptotrader.universal.library.extension.string.isDpop
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@Component
class ConsoleAuthenticationRunner @Autowired constructor(
    private val tokenVerifier: AccessTokenVerifier,
    private val productUserService: ProductUserService,
    private val tokenBlacklistService: TokenBlacklistService
) {
    private val log = LoggerFactory.getLogger(ConsoleAuthenticationRunner::class.java)

    fun <T> runWithAuthorizationHeader(authorizationHeader: String?, callbackFunction: () -> T): T {
        val previous = SecurityContextHolder.getContext().authentication
        try {
            val token: String? = this.extractAccessToken(authorizationHeader)
            if (this.isValidToken(token)) {
                this.authenticateWithToken(token)
            }
        } catch (ex: Exception) {
            log.debug("Ignoring authentication for console command due to error", ex)
            SecurityContextHolder.clearContext()
        }
        return try {
            callbackFunction()
        } finally {
            val securityContext: SecurityContext = SecurityContextHolder.getContext()
            securityContext.authentication = previous
        }
    }

    @OptIn(ExperimentalContracts::class)
    private fun isValidToken(token: String?): Boolean {
        contract {
            returns(true) implies (token != null)
        }
        return !token.isNullOrBlank() && !this.tokenBlacklistService.isBlacklisted(token)
    }

    private fun authenticateWithToken(token: String) {
        val claims: JwtClaims = this.tokenVerifier.validateAndParse(token)
        val userId: Long? = claims.subject?.toLongOrNull()
        if (userId != null) {
            val user: ProductUser? =
                this.productUserService.getUserById(userId)
            if (user != null && this.emailsMatch(claims.email, user.email)) {
                val auth = UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    user.authorities
                )
                SecurityContextHolder.getContext().authentication = auth
            } else {
                SecurityContextHolder.clearContext()
            }
        }
    }

    private fun emailsMatch(emailClaim: String?, userEmail: String?): Boolean =
        emailClaim == null || (userEmail != null && emailClaim.equals(userEmail, ignoreCase = true))

    private fun extractAccessToken(authHeader: String?): String? {
        if (authHeader.isNullOrBlank()) return null
        return when {
            authHeader.isBearer() -> authHeader.getBearerToken()
            authHeader.isDpop() -> authHeader.getDpopToken()
            else -> null
        }
    }
}