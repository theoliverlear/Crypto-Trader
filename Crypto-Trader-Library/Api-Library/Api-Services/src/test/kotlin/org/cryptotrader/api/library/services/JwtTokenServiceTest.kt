package org.cryptotrader.api.library.services

import org.cryptotrader.api.library.services.models.JwtClaims
import org.cryptotrader.test.CryptoTraderTest
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
@DisplayName("JWT Token Service")
class JwtTokenServiceTest : CryptoTraderTest() {

    lateinit var jwtTokenService: JwtTokenService

    @BeforeEach
    fun setUp() {
        this.jwtTokenService = JwtTokenService(
            "secret",
            "crypto-trader-api",
            3600
        )
    }

    @Test
    @DisplayName("Should initialize an algorithm with valid values")
    fun unit_InitializeAlgorithm_ValidValues() {
        assertDoesNotThrow {
            JwtTokenService(
                "secret",
                "crypto-trader-api",
                3600
            )
        }
    }

    @Test
    @DisplayName("Should not initialize an algorithm with invalid values")
    fun unit_InitializeAlgorithm_InvalidValues() {
        assertThrows<IllegalArgumentException> {
            JwtTokenService(
                null,
                "",
                -5
            )
        }
    }

    @Test
    @DisplayName("Should generate a token with valid values")
    fun generateToken_GeneratesToken_ValidValues() {
        val token: String = this.jwtTokenService.generateToken("1", "ollie@ollie.com")
        assert(token.isNotBlank())
        assert(token.length > 10)
    }

    @Test
    fun validateAndParse_ValidToken_ParsesToken() {
        val token: String = this.jwtTokenService.generateToken("1", "ollie@ollie.com")
        val decodedClaims: JwtClaims = this.jwtTokenService.validateAndParse(token)
        assert(decodedClaims.subject == "1")
        assert(decodedClaims.email == "ollie@ollie.com")
        assertNotNull(decodedClaims.issuedAt)
        assertNotNull(decodedClaims.expiresAt)
    }
}

