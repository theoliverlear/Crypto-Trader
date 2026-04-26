package org.cryptotrader.logging.library.communication.response

open class LogResponse(
    open val accepted: Int,
    // TODO: Refactor to enum.
    open val status: String = "accepted"
)
