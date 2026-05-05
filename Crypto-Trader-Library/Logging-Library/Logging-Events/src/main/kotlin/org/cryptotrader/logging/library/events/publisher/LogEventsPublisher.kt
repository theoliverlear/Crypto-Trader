package org.cryptotrader.logging.library.events.publisher

import org.cryptotrader.logging.library.events.LogBatchEvent
import org.cryptotrader.universal.library.events.EventPublisher
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class LogEventsPublisher @Autowired constructor(
    @Autowired(required = false)
    private val eventPublisher: EventPublisher?
) {
    private val log = LoggerFactory.getLogger(LogEventsPublisher::class.java)

    fun publishBatch(bindingName: String, batch: LogBatchEvent) {
        if (this.eventPublisher != null) {
            eventPublisher.publish(bindingName, batch)
        } else {
            log.debug("EventPublisher unavailable; skipping publishBatch in docs/non-stream context.")
        }
    }
}
