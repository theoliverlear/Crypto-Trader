package org.cryptotrader.api.controller.websocket

import com.sigwarthsoftware.springboot.websocket.WebSocketHandler
import io.github.oshai.kotlinlogging.KotlinLogging
import org.cryptotrader.api.library.comm.request.SignupRequest
import org.cryptotrader.api.library.comm.request.alias.HttpAuthStatus
import org.cryptotrader.api.library.comm.response.AuthResponse
import org.cryptotrader.api.library.services.AuthService
import org.springframework.stereotype.Component

private val log = KotlinLogging.logger {  }

@Component
class SignupWebSocketHandler(
    private val authService: AuthService
) : WebSocketHandler<SignupRequest, AuthResponse>() {
    
    override fun makeResponse(signupRequest: SignupRequest): AuthResponse {
        log.info { "Signup: ${signupRequest.email}" }
        val response: HttpAuthStatus = this.authService.signup(signupRequest)
        return AuthResponse(response.payload.isAuthorized)
    }
}