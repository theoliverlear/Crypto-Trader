package org.cryptotrader.api.config

import org.cryptotrader.console.library.communication.response.ConsoleCommandResponse
import org.cryptotrader.console.library.component.ConsoleRequestGateway
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import java.util.function.Consumer

@Configuration
open class ConsoleRepliesConfig(
    private val gateway: ConsoleRequestGateway
) {
    private val log: Logger = LoggerFactory.getLogger(ConsoleRepliesConfig::class.java)

    @Bean(name = ["consoleRepliesConsumer"]) 
    open fun consoleRepliesConsumer(): Consumer<Message<ConsoleCommandResponse>> {
        return Consumer { message ->
            log.debug("Dispatching console reply to gateway handler.")
            this.gateway.handleReply(message)
        }
    }
}
