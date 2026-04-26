package org.cryptotrader.logging.library.communication.request

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import java.time.LocalDateTime

data class FrontendLogRequest(
    @JsonDeserialize(using = FrontendTimestampDeserializer::class)
    override val timestamp: LocalDateTime,
    override val level: String,
    override val logger: String,
    override val context: String?,
    override val message: String,
    override val metadata: Map<String, Any>?,
    override val error: FrontendLogErrorRequest?
) : LogRequest(timestamp, level, logger, context, message, metadata, error)
