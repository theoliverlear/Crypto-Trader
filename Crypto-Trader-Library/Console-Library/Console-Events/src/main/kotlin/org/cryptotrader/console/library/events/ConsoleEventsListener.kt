package org.cryptotrader.console.library.events

import org.cryptotrader.console.library.communication.request.ConsoleCommandRequest
import org.cryptotrader.console.library.communication.response.ConsoleCommandResponse
import org.cryptotrader.console.library.component.ConsoleAuthenticationRunner
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
    private val consoleCommandService: ConsoleCommandService,
    private val consoleAuthenticationRunner: ConsoleAuthenticationRunner
) {
    private val log: Logger = LoggerFactory.getLogger(ConsoleEventsListener::class.java)

    @Bean(name = ["consoleRequestsConsumer"])
    open fun consoleRequestsConsumer(): Consumer<Message<ConsoleCommandRequest>> {
        return Consumer { message ->
            val command: ConsoleCommandRequest = message.payload
            log.info("Received command: {}", command)
            val correlationIdHeader = message.headers["correlationId"]
            val rawAuth: Any? = message.headers["Authorization"]
            val authorizationHeader: String? = when (rawAuth) {
                is String -> rawAuth
                is ByteArray -> try {
                    String(rawAuth, Charsets.UTF_8)
                } catch (e: Exception) { null }
                else -> null
            }
            if (authorizationHeader != null) {
                val sample = authorizationHeader.take(16) + "..."
                log.debug("Decoded Authorization header (type={}): {}", rawAuth?.javaClass?.simpleName, sample)
            } else if (rawAuth != null) {
                log.debug("Authorization header present but could not decode (type={})", rawAuth.javaClass.simpleName)
            }
            val result: ConsoleCommandResponse = try {
                this.consoleAuthenticationRunner.runWithAuthorizationHeader(authorizationHeader) {
                    this.consoleCommandService.executeCommand(command)
                }
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