package org.cryptotrader.api.controller.websocket

import com.sigwarthsoftware.springboot.websocket.WebSocketHandler
import io.github.oshai.kotlinlogging.KotlinLogging
import org.cryptotrader.api.library.comm.request.UserRequest
import org.cryptotrader.api.library.comm.response.AuthResponse
import org.cryptotrader.api.library.model.http.PayloadStatusResponse
import org.cryptotrader.api.library.services.AuthService
import org.springframework.stereotype.Component

val log = KotlinLogging.logger {  }

@Component
class SignupWebSocketHandler(
    private val authService: AuthService
) : WebSocketHandler<UserRequest, AuthResponse>() {
    
    override fun makeResponse(userRequest: UserRequest): AuthResponse {
        log.info { "Signup: ${userRequest.email}" }
        val response: PayloadStatusResponse<AuthResponse> = authService.signup(userRequest)
        return AuthResponse(response.payload.isAuthorized)
    }
}