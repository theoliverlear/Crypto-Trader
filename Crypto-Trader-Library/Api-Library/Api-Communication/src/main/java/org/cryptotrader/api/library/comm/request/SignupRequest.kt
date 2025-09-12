package org.cryptotrader.api.library.comm.request

data class SignupRequest(
    val email: String,
    val username: String,
    val password: String,
)