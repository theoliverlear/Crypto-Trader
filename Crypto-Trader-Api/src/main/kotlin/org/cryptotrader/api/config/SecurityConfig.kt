package org.cryptotrader.api.config

import org.cryptotrader.security.library.service.InMemoryIpBanService
import org.cryptotrader.security.library.service.IpBanService
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
open class SecurityConfig {
    //------------------------Security-Filter-Chain---------------------------
    @Bean
    @Primary
    open fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers("/ws/signup", "/ws/login").permitAll()
                    .anyRequest().permitAll()
            }.build()
    }

    @Bean
    @ConditionalOnMissingBean(IpBanService::class)
    open fun ipBanService(): IpBanService = InMemoryIpBanService()
}