package org.cryptotrader.logging.library.communication.response

data class FrontendLogResponse(
    override val accepted: Int,
    // TODO: Refactor to enum.
    override val status: String = "accepted"
) : LogResponse(accepted, status)
