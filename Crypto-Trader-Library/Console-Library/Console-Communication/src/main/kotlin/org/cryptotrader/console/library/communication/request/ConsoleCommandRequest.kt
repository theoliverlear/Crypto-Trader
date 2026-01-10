package org.cryptotrader.console.library.communication.request

data class ConsoleCommandRequest(
    val commandText: String,
    var traceId: String?
)