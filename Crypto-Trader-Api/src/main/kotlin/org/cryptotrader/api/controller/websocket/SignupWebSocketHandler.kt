package org.cryptotrader.api.controller.websocket

import com.sigwarthsoftware.springboot.websocket.WebSocketHandler
import org.slf4j.LoggerFactory
import org.cryptotrader.api.library.communication.request.SignupRequest
import org.cryptotrader.api.library.communication.request.alias.HttpAuthStatus
import org.cryptotrader.api.library.communication.response.AuthResponse
import org.cryptotrader.api.library.services.AuthService
import org.springframework.stereotype.Component

private val log = LoggerFactory.getLogger(SignupWebSocketHandler::class.java)

@Component
class SignupWebSocketHandler(
    private val authService: AuthService
) : WebSocketHandler<SignupRequest, AuthResponse>() {
    
    override fun makeResponse(signupRequest: SignupRequest): AuthResponse {
        log.info("Signup: {}", signupRequest.email)
        val response: HttpAuthStatus = this.authService.signup(signupRequest)
        return response.payload
    }
}