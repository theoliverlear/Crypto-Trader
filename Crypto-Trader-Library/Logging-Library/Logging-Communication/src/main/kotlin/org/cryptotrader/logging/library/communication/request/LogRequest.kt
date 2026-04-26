package org.cryptotrader.logging.library.communication.request

import java.time.LocalDateTime

open class LogRequest(
    open val timestamp: LocalDateTime,
    open val level: String,
    open val logger: String,
    open val context: String?,
    open val message: String,
    open val metadata: Map<String, Any>?,
    open val error: LogErrorRequest?
)
