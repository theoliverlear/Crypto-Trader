package org.cryptotrader.api.config

import org.cryptotrader.api.controller.websocket.SignupWebSocketHandler
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.server.support.DefaultHandshakeHandler

@Configuration
@EnableWebSocket
open class WebSocketConfig(
    val signupWebsocket: SignupWebSocketHandler
) : WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(this.signupWebsocket, "/ws/signup")
                .setAllowedOrigins("*")
                .setHandshakeHandler(DefaultHandshakeHandler())
    }
}