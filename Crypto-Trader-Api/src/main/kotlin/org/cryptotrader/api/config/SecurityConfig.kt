package org.cryptotrader.api.config

import org.cryptotrader.api.library.infrastructure.JwtAuthenticationFilter
import org.cryptotrader.api.library.infrastructure.dpop.DpopValidationFilter
import org.cryptotrader.api.library.infrastructure.dpop.BindingEnforcementFilter
import org.cryptotrader.security.library.service.InMemoryIpBanService
import org.cryptotrader.security.library.service.IpBanService
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.config.http.SessionCreationPolicy

/**
 * Application Security configuration.
 *
 * - Enforces DPoP for DPoP-bound tokens by placing DPoPValidationFilter before JWT authentication.
 * - Requires authentication for all endpoints except explicit allow-list (docs, auth, websockets, JWKS).
 * - Enables CORS with credentialed requests only for refresh/logout; HSTS and a minimal CSP are applied.
 */
@Configuration
@EnableWebSecurity
open class SecurityConfig {
    //------------------------Security-Filter-Chain---------------------------
    @Bean
    @Primary
    open fun apiSecurityFilterChain(
        http: HttpSecurity,
        dpopValidationFilter: DpopValidationFilter,
        jwtAuthenticationFilter: JwtAuthenticationFilter,
        bindingEnforcementFilter: BindingEnforcementFilter
    ): SecurityFilterChain {
        return http
            .csrf { it.disable() }
            .cors { }
            .headers { headers ->
                headers.httpStrictTransportSecurity { transportPolicy ->
                    transportPolicy.includeSubDomains(true).preload(true).maxAgeInSeconds(31536000)
                }
                headers.contentSecurityPolicy { securityPolicy ->
                    securityPolicy.policyDirectives("default-src 'self'; script-src 'self'; object-src 'none'; frame-ancestors 'none'; base-uri 'self'")
                }
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it.requestMatchers(
                    "/ws/signup",
                    "/ws/login",
                    "/ws/currency/value",
                    "/api/auth/**",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/actuator/**",
                    "/.well-known/jwks.json"
                ).permitAll()
                it.anyRequest().authenticated()
            }
            // Validate DPoP before we attempt to authenticate the access token
            .addFilterBefore(dpopValidationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            // Enforce binding AFTER JWT authentication has taken place
            .addFilterAfter(bindingEnforcementFilter, JwtAuthenticationFilter::class.java)
            .build()
    }

    @Bean
    @ConditionalOnMissingBean(IpBanService::class)
    open fun ipBanService(): IpBanService = InMemoryIpBanService()

    @Bean
    @ConditionalOnMissingBean(CorsConfigurationSource::class)
    open fun corsConfigurationSource(@Value("\${cryptotrader.cors.allowed-origins:https://sscryptotrader.com}") origins: String): CorsConfigurationSource {
        val allowedOrigins: List<String> = origins.split(",").map {
            it.trim()
        }.filter { it.isNotBlank() }

        val base = CorsConfiguration()
        base.allowedOrigins = allowedOrigins
        base.allowedMethods = listOf("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        base.allowedHeaders = listOf("Authorization", "Content-Type", "DPoP")
        base.allowCredentials = true

        val auth = CorsConfiguration()
        auth.allowedOrigins = allowedOrigins
        auth.allowedMethods = base.allowedMethods
        auth.allowedHeaders = base.allowedHeaders
        auth.allowCredentials = true

        val source = UrlBasedCorsConfigurationSource()
        // All endpoints (no credentials by default)
        source.registerCorsConfiguration("/**", base)
        // Allow credentials only for refresh/logout
        source.registerCorsConfiguration("/api/auth/refresh", auth)
        source.registerCorsConfiguration("/api/auth/logout", auth)
        return source
    }
}