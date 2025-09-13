package org.cryptotrader.api.library.communication.request

data class SignupRequest(
    val email: String,
    val username: String,
    val password: String,
)