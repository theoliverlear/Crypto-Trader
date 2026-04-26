package org.cryptotrader.logging.library.communication.request

open class LogErrorRequest(
    open val name: String?,
    open val message: String?,
    open val stack: String?
)
