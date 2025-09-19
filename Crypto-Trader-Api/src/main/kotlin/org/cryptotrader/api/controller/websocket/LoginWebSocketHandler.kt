package org.cryptotrader.api.controller.websocket

import com.sigwarthsoftware.springboot.websocket.WebSocketHandler
import org.slf4j.LoggerFactory
import org.cryptotrader.api.library.communication.request.LoginRequest
import org.cryptotrader.api.library.communication.request.alias.HttpAuthStatus
import org.cryptotrader.api.library.communication.response.AuthResponse
import org.cryptotrader.api.library.services.AuthService
import org.springframework.stereotype.Component

private val log = LoggerFactory.getLogger(LoginWebSocketHandler::class.java)

@Component
class LoginWebSocketHandler(
    private val authService: AuthService
) : WebSocketHandler<LoginRequest, AuthResponse>() {
    override fun makeResponse(loginRequest: LoginRequest): AuthResponse {
        log.info("Login: {}", loginRequest.email)
        val response: HttpAuthStatus = this.authService.login(loginRequest)
        return AuthResponse(response.payload.isAuthorized)
    }
}