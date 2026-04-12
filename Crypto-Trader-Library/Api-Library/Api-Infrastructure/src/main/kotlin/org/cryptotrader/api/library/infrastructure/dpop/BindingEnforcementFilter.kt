package org.cryptotrader.api.library.infrastructure.dpop

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.cryptotrader.api.library.infrastructure.dpop.DpopValidationFilter.Companion.REQ_ATTR_PROOF_JKT
import org.cryptotrader.api.library.infrastructure.dpop.DpopValidationFilter.Companion.REQ_ATTR_PROOF
import org.cryptotrader.api.library.infrastructure.extension.getAuthHeader
import org.cryptotrader.api.library.infrastructure.extension.getDpopToken
import org.cryptotrader.api.library.infrastructure.extension.isDpop
import org.cryptotrader.api.library.services.jwt.JwtTokenService
import org.cryptotrader.api.library.model.dpop.DpopProofContext
import org.cryptotrader.api.library.model.jwt.JwtClaims
import org.cryptotrader.api.library.extensions.sendUnauthorized
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

/**
 * Filter for enforcing cryptographic binding between access tokens and DPoP proofs.
 */
@Component
class BindingEnforcementFilter(
    private val jwtTokenService: JwtTokenService
) : OncePerRequestFilter() {

    companion object {
        private val log = LoggerFactory.getLogger(BindingEnforcementFilter::class.java)
    }

    override fun doFilterInternal(request: HttpServletRequest,
                                  response: HttpServletResponse,
                                  filterChain: FilterChain) {
        val authorization: String = request.getAuthHeader() ?: run {
            filterChain.doFilter(request, response)
            return
        }

        if (authorization.isDpop()) {
            val token: String = authorization.getDpopToken()
            try {
                val claims: JwtClaims = this.jwtTokenService.validateAndParse(token)
                val tokenJkt: String? = claims.confirmJkt
                val proofContext: DpopProofContext? = request.getAttribute(REQ_ATTR_PROOF) as? DpopProofContext
                val proofJkt: String? = proofContext?.keyThumbprint ?: (request.getAttribute(REQ_ATTR_PROOF_JKT) as String?)
                if (!tokenJkt.isNullOrBlank()) {
                    if (this.isTokenJktValid(proofJkt, tokenJkt)) {
                        log.debug("cnf mismatch: tokenJkt={}, proofJkt={}", tokenJkt, proofJkt)
                        response.sendUnauthorized("cnf_mismatch", "Access token cnf.jkt does not match DPoP key", "DPoP")
                        return
                    }
                }
            } catch (ex: Exception) {
                // Let downstream handlers decide (e.g., JWT expired).
                // We do not enforce binding if token invalid.
            }
        }
        filterChain.doFilter(request, response)
    }

    private fun isTokenJktValid(proofJkt: String?, tokenJkt: String): Boolean =
        proofJkt.isNullOrBlank() || tokenJkt != proofJkt
}