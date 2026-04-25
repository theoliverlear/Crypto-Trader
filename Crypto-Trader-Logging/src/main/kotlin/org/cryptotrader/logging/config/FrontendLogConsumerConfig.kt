package org.cryptotrader.logging.config

import org.cryptotrader.logging.library.events.FrontendLogBatchEvent
import org.cryptotrader.logging.library.service.FrontendLogPersistenceService
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import java.util.function.Consumer

@Configuration
open class FrontendLogConsumerConfig(
    private val persistenceService: FrontendLogPersistenceService
) {
    private val log = LoggerFactory.getLogger(FrontendLogConsumerConfig::class.java)

    @Bean(name = ["frontendLogsConsumer"])
    open fun frontendLogsConsumer(): Consumer<Message<FrontendLogBatchEvent>> {
        return Consumer { message ->
            val batch = message.payload
            log.info("Received {} frontend log entries", batch.entries.size)
            persistenceService.persist(batch.entries, batch.receivedAt)
        }
    }
}
