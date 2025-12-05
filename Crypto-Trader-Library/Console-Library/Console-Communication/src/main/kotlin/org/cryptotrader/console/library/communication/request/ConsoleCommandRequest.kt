package org.cryptotrader.console.library.communication.request

data class ConsoleCommandRequest(
    val command: String,
    var traceId: String?
)