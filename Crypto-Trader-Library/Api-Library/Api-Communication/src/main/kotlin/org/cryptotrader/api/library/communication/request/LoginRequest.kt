package org.cryptotrader.api.library.communication.request

data class LoginRequest(
    val email: String,
    val password: String
)