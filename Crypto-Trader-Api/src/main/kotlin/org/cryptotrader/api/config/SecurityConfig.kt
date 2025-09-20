package org.cryptotrader.api.config

import org.cryptotrader.api.library.infrastructure.JwtAuthenticationFilter
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

@Configuration
@EnableWebSecurity
open class SecurityConfig {
    //------------------------Security-Filter-Chain---------------------------
    @Bean
    @Primary
    open fun apiSecurityFilterChain(http: HttpSecurity,
                                    jwtAuthenticationFilter: JwtAuthenticationFilter): SecurityFilterChain {
        return http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers(
                    "/ws/signup",
                    "/ws/login",
                    "/ws/currency/value",
                    "/api/auth/**",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/actuator/**"
                ).permitAll()
                    .anyRequest().permitAll()
            }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }

    @Bean
    @ConditionalOnMissingBean(IpBanService::class)
    open fun ipBanService(): IpBanService = InMemoryIpBanService()
}