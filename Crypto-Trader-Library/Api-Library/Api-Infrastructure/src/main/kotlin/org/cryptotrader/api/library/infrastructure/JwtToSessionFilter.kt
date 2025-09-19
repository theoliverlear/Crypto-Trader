package org.cryptotrader.api.library.infrastructure

import org.slf4j.LoggerFactory
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import org.cryptotrader.api.library.entity.user.ProductUser
import org.cryptotrader.api.library.services.JwtClaims
import org.cryptotrader.api.library.services.JwtService
import org.cryptotrader.api.library.services.ProductUserService
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.text.startsWith
import kotlin.text.substringAfter

private val log = LoggerFactory.getLogger(JwtToSessionFilter::class.java)

@Component
class JwtToSessionFilter(private val jwtService: JwtService, 
                         @param:Lazy private val productUserService: ProductUserService
) : OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest,
                                  response: HttpServletResponse,
                                  filterChain: FilterChain) {
        val authHeader: String? = request.getHeader("Authorization")
        if (this.isValidAuthHeader(authHeader)) {
            val token: String = authHeader.substringAfter("Bearer ")
            try {
                val claims: JwtClaims? = jwtService.validateAndParse(token)
                val userIdStr = claims!!.subject
                val email = claims.email
                val userId = userIdStr?.toLongOrNull()
                if (userId != null) {
                    val fetched = productUserService.getUserById(userId)
                    val user: ProductUser? = fetched as? ProductUser
                    if (this.isValidUser(email, user)) {
                        val session: HttpSession? = request.session
                        this.addUserToSession(session, user)
                    }

                }
            } catch (ex: Exception) {
                log.error("Error validating JWT", ex)
            }
        }
        filterChain.doFilter(request, response)
    }

    private fun addUserToSession(session: HttpSession?, user: ProductUser?) {
        if (session != null && user != null) {
            session.setAttribute("product-user", user)
            session.setAttribute("user", user)
        }
    }

    private fun isValidUser(email: String?, user: ProductUser?): Boolean {
        return user != null &&
                email == null ||
                email.equals(user?.email, ignoreCase = true)
    }

    @OptIn(ExperimentalContracts::class)
    private fun isValidAuthHeader(authHeader: String?): Boolean {
        contract {
            returns(true) implies (authHeader != null)
        }
        return authHeader != null && authHeader.startsWith("Bearer ")
    }
}
