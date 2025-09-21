package org.cryptotrader.api.config

import org.cryptotrader.api.controller.websocket.CurrencyValueWebSocketHandler
import org.cryptotrader.api.controller.websocket.LoginWebSocketHandler
import org.cryptotrader.api.controller.websocket.SignupWebSocketHandler
import org.cryptotrader.api.library.infrastructure.JwtHandshakeInterceptor
import org.cryptotrader.api.library.services.jwt.JwtTokenService
import org.cryptotrader.api.library.services.ProductUserService
import org.cryptotrader.api.library.services.jwt.TokenBlacklistService
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.server.support.DefaultHandshakeHandler

@Configuration
@EnableWebSocket
@Import(JwtHandshakeInterceptor::class)
@ConditionalOnProperty(name = ["cryptotrader.api.websocket.enabled"], havingValue = "true", matchIfMissing = true)
open class WebSocketConfig(
    val signupWebsocket: SignupWebSocketHandler,
    val loginWebSocket: LoginWebSocketHandler,
    val currencyValueWebSocket: CurrencyValueWebSocketHandler,
    val jwtHandshakeInterceptor: JwtHandshakeInterceptor
) : WebSocketConfigurer {

    @Bean
    @ConditionalOnMissingBean(JwtHandshakeInterceptor::class)
    open fun jwtHandshakeInterceptor(jwtService: JwtTokenService,
                                     productUserService: ProductUserService,
                                     tokenBlacklistService: TokenBlacklistService
    ): JwtHandshakeInterceptor =
        JwtHandshakeInterceptor(jwtService, productUserService, tokenBlacklistService)

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(this.signupWebsocket, "/ws/signup")
            .addInterceptors(this.jwtHandshakeInterceptor)
            .setAllowedOriginPatterns("*")
            .setHandshakeHandler(DefaultHandshakeHandler())

        registry.addHandler(this.loginWebSocket, "/ws/login")
            .addInterceptors(this.jwtHandshakeInterceptor)
            .setAllowedOriginPatterns("*")
            .setHandshakeHandler(DefaultHandshakeHandler())
        registry.addHandler(this.currencyValueWebSocket, "/ws/currency/value")
            .addInterceptors(this.jwtHandshakeInterceptor)
            .setAllowedOriginPatterns("*")
            .setHandshakeHandler(DefaultHandshakeHandler())
    }
}