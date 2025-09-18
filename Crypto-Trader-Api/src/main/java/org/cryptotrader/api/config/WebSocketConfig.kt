package org.cryptotrader.api.config

import org.cryptotrader.api.controller.websocket.LoginWebSocketHandler
import org.cryptotrader.api.controller.websocket.SignupWebSocketHandler
import org.cryptotrader.api.library.infrastructure.JwtHandshakeInterceptor
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.server.support.DefaultHandshakeHandler

@Configuration
@EnableWebSocket
@ConditionalOnProperty(name = ["cryptotrader.api.websocket.enabled"], havingValue = "true", matchIfMissing = true)
open class WebSocketConfig(
    val signupWebsocket: SignupWebSocketHandler,
    val loginWebSocket: LoginWebSocketHandler,
    val jwtHandshakeInterceptor: JwtHandshakeInterceptor
) : WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(this.signupWebsocket, "/ws/signup")
            .addInterceptors(jwtHandshakeInterceptor)
            .setAllowedOrigins("*")
            .setHandshakeHandler(DefaultHandshakeHandler())

        registry.addHandler(this.loginWebSocket, "/ws/login")
            .addInterceptors(jwtHandshakeInterceptor)
            .setAllowedOrigins("*")
            .setHandshakeHandler(DefaultHandshakeHandler())
    }
}