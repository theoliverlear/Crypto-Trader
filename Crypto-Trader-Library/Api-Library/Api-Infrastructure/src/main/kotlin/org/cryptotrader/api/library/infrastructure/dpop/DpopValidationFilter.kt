package org.cryptotrader.api.library.infrastructure.dpop

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.web.filter.OncePerRequestFilter
import org.cryptotrader.api.library.model.dpop.DpopProofContext
import com.auth0.jwt.JWT
import org.cryptotrader.api.library.component.dpop.DpopReplayCache
import org.cryptotrader.api.library.infrastructure.extension.getAuthHeader
import org.cryptotrader.api.library.infrastructure.extension.getBearerToken
import org.cryptotrader.api.library.infrastructure.extension.getDpopHeader
import org.cryptotrader.api.library.infrastructure.extension.getDpopToken
import org.cryptotrader.api.library.infrastructure.extension.isBearer
import org.cryptotrader.api.library.infrastructure.extension.isDpop
import org.cryptotrader.api.library.services.jwt.JwtTokenService
import org.cryptotrader.api.library.model.jwt.JwtClaims
import org.cryptotrader.api.library.scripts.http.HttpErrorScript
import org.cryptotrader.api.library.scripts.http.HttpScript
import org.cryptotrader.api.library.services.dpop.DpopVerifierService

/**
 * DPoPValidationFilter (runs before JWT authentication).
 *
 * Purpose in simple terms:
 * - When a request says “I’m using a DPoP token”, this filter checks the extra proof that the browser sends.
 * - That proof is like a signed receipt that includes the HTTP method, URL, and a fresh random id so it can’t be replayed.
 *
 * What this filter checks:
 * - There is a DPoP header if Authorization uses the "DPoP <token>" scheme.
 * - The proof’s digital signature is valid (we rebuild a public key from the header and verify the signature).
 * - htm matches the HTTP method; htu matches the full request URL (scheme://host[:port]/path?query).
 * - iat (issued-at) is recent (within a small clock window), and jti (a one-time random id) has not been seen before.
 * - If an access token is present, ath (access token hash) must match, proving the proof was made for that token.
 * - We compute a short fingerprint of the public key (jkt) and store it on the request for the next filter.
 * - If someone tries to send a DPoP-bound token with Authorization: Bearer (no proof), we reject it.
 *
 * Notes:
 * - This filter does not decide if the access token itself is valid or expired; the JWT filter does that.
 * - jti means “JWT ID” here: a unique nonce we keep briefly to prevent replay.
 */
@ConditionalOnProperty(name = ["security.auth.dpop.enabled"], havingValue = "true", matchIfMissing = true)
@Component
class DpopValidationFilter(
    private val dpopVerifier: DpopVerifierService,
    private val replayCache: DpopReplayCache,
    private val jwtTokenService: JwtTokenService
) : OncePerRequestFilter() {

    companion object {
        private val log = LoggerFactory.getLogger(DpopValidationFilter::class.java)
        const val REQ_ATTR_PROOF: String = "dpop.proof"
        const val REQ_ATTR_PROOF_JKT: String = "dpop.proof.jkt"
    }

    override fun doFilterInternal(request: HttpServletRequest,
                                  response: HttpServletResponse,
                                  filterChain: FilterChain) {
        // Allow unauthenticated status check without DPoP enforcement
        val uriString: String = request.requestURI ?: ""
        if (this.isException(uriString)) {
            filterChain.doFilter(request, response)
            return
        }
        val authorizationHeader: String? = request.getAuthHeader()
        val isDpopAuth: Boolean = authorizationHeader?.isDpop() ?: false
        if (!isDpopAuth) {
            // Enforce DPoP usage for DPoP-bound tokens (those carrying cnf.jkt)
            val bearerHeader: String? = request.getAuthHeader()
            if (bearerHeader?.isBearer() ?: false) {
                val accessToken: String = bearerHeader.getBearerToken()
                try {
                    val claims: JwtClaims = this.jwtTokenService.validateAndParse(accessToken)
                    if (!claims.confirmJkt.isNullOrBlank()) {
                        log.debug("Bearer token with cnf.jkt used without DPoP proof; rejecting")
                        HttpErrorScript.unauthorized(response, "dpop_required", "DPoP proof required for bound token", "DPoP")
                        return
                    }
                } catch (_: Exception) {
                    // Let downstream filters handle invalid/expired tokens
                }
            }
            filterChain.doFilter(request, response)
            return
        }

        val accessToken: String = authorizationHeader.getDpopToken()
        val dpopHeader: String? = request.getDpopHeader()
        if (dpopHeader.isNullOrBlank()) {
            log.debug("DPoP header missing while using DPoP authorization scheme")
            HttpErrorScript.unauthorized(response, "invalid_dpop", "Missing DPoP proof", "DPoP")
            return
        }

        try {
            // Verify DPoP proof cryptographically and validate claims (require 'ath' when token provided)
            val verification: DpopVerifierService.VerificationResult? = 
                this.dpopVerifier.verify(dpopHeader, request.method, HttpScript.fullUrl(request), accessToken)
            if (verification == null) {
                log.debug("DPoP proof verification failed")
                HttpErrorScript.unauthorized(response, "invalid_dpop", "Verification failed", "DPoP")
                return
            }
            // Replay detection by jti
            if (this.replayCache.isReplay(verification.jwtId)) {
                log.debug("DPoP proof replay detected for jti={}", verification.jwtId)
                HttpErrorScript.unauthorized(response, "replay", "DPoP proof replay detected", "DPoP")
                return
            }

            val issuedAt: Long = try {
                JWT.decode(dpopHeader).getClaim("iat").asLong() ?: 0L
            } catch (_: Exception) {
                0L
            }
            val dpopProofContext = DpopProofContext(
                keyThumbprint = verification.jwkThumbprint,
                proofNonce = verification.jwtId,
                httpMethod = request.method,
                httpUrl = HttpScript.fullUrl(request),
                issuedAtEpochSeconds = issuedAt
            )
            request.setAttribute(REQ_ATTR_PROOF, dpopProofContext)
            request.setAttribute(REQ_ATTR_PROOF_JKT, verification.jwkThumbprint)
        } catch (ex: Exception) {
            log.debug("DPoP validation failed", ex)
            HttpErrorScript.unauthorized(response, "invalid_dpop", "Validation exception", "DPoP")
            return
        }
        filterChain.doFilter(request, response)
    }

    private fun isException(uriString: String): Boolean =
        uriString.startsWith("/api/auth/logout") ||
                uriString.endsWith("/api/auth/logged-in")
}