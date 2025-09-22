package org.cryptotrader.api.library.services

import org.cryptotrader.api.library.model.jwt.JwtClaims
import org.cryptotrader.api.library.services.jwt.JwtTokenService
import org.cryptotrader.api.library.services.rsa.RsaKeyService
import org.cryptotrader.test.CryptoTraderTest
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
@DisplayName("JWT Token Service")
class JwtTokenServiceTest : CryptoTraderTest() {

    lateinit var jwtTokenService: JwtTokenService

    @BeforeEach
    fun setUp() {
        val rsa = RsaKeyService(null, null, null)
        this.jwtTokenService = JwtTokenService(
            rsa,
            "https://sscryptotrader.com",
            300,
            "crypto-trader-api"
        )
    }

    @Test
    @DisplayName("Should initialize service with valid values")
    fun unit_Initialize_ValidValues() {
        assertDoesNotThrow {
            val rsa = RsaKeyService(null, null, null)
            JwtTokenService(
                rsa,
                "https://api.cryptotrader.com",
                300,
                "crypto-trader-api"
            )
        }
    }

    @Test
    @DisplayName("Should generate a token with valid values and parse claims")
    fun generateAndParse_GeneratesAndParsesToken() {
        val token: String = this.jwtTokenService.generateToken("1", "ollie@ollie.com")
        assert(token.isNotBlank())
        val decodedClaims: JwtClaims = this.jwtTokenService.validateAndParse(token)
        assert(decodedClaims.subject == "1")
        assert(decodedClaims.email == "ollie@ollie.com")
        assertNotNull(decodedClaims.issuedAt)
        assertNotNull(decodedClaims.expiresAt)
    }
}

