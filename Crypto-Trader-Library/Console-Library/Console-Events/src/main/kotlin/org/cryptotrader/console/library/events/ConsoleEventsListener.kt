package org.cryptotrader.console.library.events

import org.cryptotrader.console.library.communication.request.ConsoleCommandRequest
import org.cryptotrader.console.library.communication.response.ConsoleCommandResponse
import org.cryptotrader.console.library.services.ConsoleCommandService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.support.MessageBuilder
import org.springframework.messaging.Message
import java.util.function.Consumer

@Configuration
open class ConsoleEventsListener @Autowired constructor(
    private val streamBridge: StreamBridge,
    private val consoleCommandService: ConsoleCommandService
) {
    private val log: Logger = LoggerFactory.getLogger(ConsoleEventsListener::class.java)

    @Bean(name = ["consoleRequestsConsumer"])
    open fun consoleRequestsConsumer(): Consumer<Message<ConsoleCommandRequest>> {
        return Consumer { message ->
            val command: ConsoleCommandRequest = message.payload
            log.info("Received command: {}", command)
            val correlationIdHeader = message.headers["correlationId"]
            val result: ConsoleCommandResponse = try {
                this.consoleCommandService.executeCommand(command) 
            } catch (exception: Exception) {
                ConsoleCommandResponse(
                    "An error occurred: ${exception.message}",
                    null
                )
            }
            val replyMessage = MessageBuilder
                .withPayload(result)
                .setHeader("correlationId", correlationIdHeader)
                .build()
            this.streamBridge.send("consoleReplies-out-0", replyMessage)
        }
    }
}