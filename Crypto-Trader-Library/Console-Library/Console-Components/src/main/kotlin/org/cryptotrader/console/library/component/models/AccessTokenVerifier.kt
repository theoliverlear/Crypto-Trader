package org.cryptotrader.console.library.component.models

import org.cryptotrader.api.library.model.jwt.JwtClaims

/**
 * Minimal abstraction for validating and parsing an access token into JwtClaims.
 * Implementations may verify via local RSA keys or a remote JWKS.
 */
interface AccessTokenVerifier {
    fun validateAndParse(token: String): JwtClaims
}