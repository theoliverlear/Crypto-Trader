package org.cryptotrader.logging.library.events

import java.time.LocalDateTime

class FrontendLogBatchEvent(
    override val entries: List<FrontendLogEvent>,
    receivedAt: LocalDateTime
) : LogBatchEvent(entries, receivedAt)
