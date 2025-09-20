package org.cryptotrader.api.library.infrastructure

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.assertj.core.api.Assertions.assertThat
import org.cryptotrader.api.library.entity.user.ProductUser
import org.cryptotrader.api.library.services.JwtTokenService
import org.cryptotrader.api.library.services.ProductUserService
import org.cryptotrader.test.CryptoTraderTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

@Tag("Infrastructure")
@Tag("JwtAuthenticationFilter")
@DisplayName("JWT Authentication Filter")
class JwtAuthenticationFilterTest : CryptoTraderTest() {
    private lateinit var jwtService: JwtTokenService
    private lateinit var productUserService: ProductUserService
    private lateinit var filter: JwtAuthenticationFilter

    val testSecret: String = "secret"
    val testIssuer: String = "issuer"
    val testExpiration: Long = 3600
    val testPrincipal: String = "user"
    val testCredentials: String = "cred"
    val testUserId: Long = 10L
    val testEmail: String = "ollie@ollie.com"
    
    @BeforeEach
    fun setUp() {
        this.jwtService = JwtTokenService(this.testSecret, this.testIssuer, this.testExpiration)
        this.productUserService = mock(ProductUserService::class.java)
        this.filter = JwtAuthenticationFilter(this.jwtService, this.productUserService)
        SecurityContextHolder.clearContext()
    }

    @AfterEach
    fun tearDown() {
        SecurityContextHolder.clearContext()
    }

    @Test
    @Tag("authentication")
    @DisplayName("Should have no authorization header clear context and continues chain")
    fun authentication_ClearsContextContinuesChain_NoAuthHeader() {
        val request: HttpServletRequest = mock(HttpServletRequest::class.java)
        val response: HttpServletResponse = mock(HttpServletResponse::class.java)
        val chain: FilterChain = mock(FilterChain::class.java)
        `when`(request.getHeader("Authorization")).thenReturn(null)

        val testingAuthenticationToken = TestingAuthenticationToken(
            this.testPrincipal,
            this.testCredentials
        )
        SecurityContextHolder.getContext().authentication = testingAuthenticationToken
        this.filter.doFilter(request, response, chain)
        verify(chain, times(1)).doFilter(request, response)
        assertThat(SecurityContextHolder.getContext().authentication).isNull()
    }

    @Test
    @DisplayName("Should have invalid token clear context and continues chain")
    fun authentication_ClearsContextContinuesChain_InvalidToken() {
        val request = mock(HttpServletRequest::class.java)
        val response = mock(HttpServletResponse::class.java)
        val chain = mock(FilterChain::class.java)

        `when`(request.getHeader("Authorization")).thenReturn("Bearer not-a-valid-token")

        // Pre-populate context to ensure it gets cleared on failure
        SecurityContextHolder.getContext().authentication =
            TestingAuthenticationToken("user","cred")

        filter.doFilter(request, response, chain)

        verify(chain, times(1)).doFilter(request, response)
        assertThat(SecurityContextHolder.getContext().authentication).isNull()
    }

    @Test
    fun `valid token with matching user authenticates request`() {
        val request = mock(HttpServletRequest::class.java)
        val response = mock(HttpServletResponse::class.java)
        val chain = mock(FilterChain::class.java)

        val userId = 42L
        val email = "test@example.com"
        val token = jwtService.generateToken(userId.toString(), email)
        `when`(request.getHeader("Authorization")).thenReturn("Bearer $token")

        val user = ProductUser.builder().email(email).build()
        user.id = userId
        `when`(productUserService.getUserById(userId)).thenReturn(user)

        filter.doFilter(request, response, chain)

        verify(chain, times(1)).doFilter(request, response)
        val auth = SecurityContextHolder.getContext().authentication
        assertThat(auth).isNotNull
        assertThat(auth!!.isAuthenticated).isTrue()
        assertThat(auth.principal).isEqualTo(user)
    }

    @Test
    fun `valid token but user not found clears context`() {
        val request = mock(HttpServletRequest::class.java)
        val response = mock(HttpServletResponse::class.java)
        val chain = mock(FilterChain::class.java)

        val userId = 99L
        val email = "someone@example.com"
        val token = jwtService.generateToken(userId.toString(), email)
        `when`(request.getHeader("Authorization")).thenReturn("Bearer $token")

        `when`(productUserService.getUserById(userId)).thenReturn(null)

        filter.doFilter(request, response, chain)

        verify(chain, times(1)).doFilter(request, response)
        assertThat(SecurityContextHolder.getContext().authentication).isNull()
    }

    @Test
    fun `valid token but email mismatch clears context`() {
        val request = mock(HttpServletRequest::class.java)
        val response = mock(HttpServletResponse::class.java)
        val chain = mock(FilterChain::class.java)

        val userId = 7L
        val tokenEmail = "token@example.com"
        val userEmail = "real@example.com"
        val token = jwtService.generateToken(userId.toString(), tokenEmail)
        `when`(request.getHeader("Authorization")).thenReturn("Bearer $token")

        val user = ProductUser.builder().email(userEmail).build()
        user.id = userId
        `when`(this.productUserService.getUserById(userId)).thenReturn(user)

        filter.doFilter(request, response, chain)

        verify(chain, times(1)).doFilter(request, response)
        assertThat(SecurityContextHolder.getContext().authentication).isNull()
    }
}
