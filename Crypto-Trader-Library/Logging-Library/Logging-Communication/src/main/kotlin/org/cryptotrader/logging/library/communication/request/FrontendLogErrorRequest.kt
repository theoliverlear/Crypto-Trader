package org.cryptotrader.logging.library.communication.request

data class FrontendLogErrorRequest(
    override val name: String?,
    override val message: String?,
    override val stack: String?
) : LogErrorRequest(name, message, stack)
