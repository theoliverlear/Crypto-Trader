package org.cryptotrader.api.library.comm.request

data class LoginRequest(
    val email: String,
    val password: String
)