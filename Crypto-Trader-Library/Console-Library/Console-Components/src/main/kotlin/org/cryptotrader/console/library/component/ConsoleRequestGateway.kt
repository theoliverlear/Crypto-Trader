package org.cryptotrader.console.library.component

import org.cryptotrader.console.library.communication.request.ConsoleCommandRequest
import org.cryptotrader.console.library.communication.response.ConsoleCommandResponse
import org.cryptotrader.universal.library.component.EventPublisher
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.Message
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import java.time.Duration
import java.util.UUID
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@Component
class ConsoleRequestGateway @Autowired constructor(
    private val eventPublisher: EventPublisher
) {
    private val log: Logger = LoggerFactory.getLogger(ConsoleRequestGateway::class.java)
    private val pendingReplies: MutableMap<String, CompletableFuture<ConsoleCommandResponse>> =
        ConcurrentHashMap()

    fun execute(
        consoleTextCommand: ConsoleCommandRequest,
        timeout: Duration = Duration.ofSeconds(15)
    ): ConsoleCommandResponse {
        return this.execute(consoleTextCommand, timeout, null)
    }

    fun execute(
        consoleTextCommand: ConsoleCommandRequest,
        timeout: Duration = Duration.ofSeconds(15),
        authorizationHeader: String?
    ): ConsoleCommandResponse {

        val correlationId: String = UUID.randomUUID().toString()
        val future = CompletableFuture<ConsoleCommandResponse>()
        this.pendingReplies[correlationId] = future

        val headersMutable = mutableMapOf<String, Any>(
            "correlationId" to correlationId
        )
        if (!authorizationHeader.isNullOrBlank()) {
            headersMutable["Authorization"] = authorizationHeader
        }
        val headers: Map<String, Any> = headersMutable

        log.debug(
            "Publishing console command correlationId={} text={}",
            correlationId,
            consoleTextCommand
        )

        this.eventPublisher.publish("consoleRequests-out-0", consoleTextCommand, headers)

        return try {
            future.get(timeout.toMillis(), TimeUnit.MILLISECONDS)
        } catch (exception: TimeoutException) {
            this.pendingReplies.remove(correlationId)
            log.warn("Timeout waiting for console reply correlationId={}", correlationId)
            throw exception
        } catch (exception: Exception) {
            this.pendingReplies.remove(correlationId)
            log.error("Error waiting for console reply correlationId={}", correlationId, exception)
            throw exception
        }
    }

    fun handleReply(message: Message<ConsoleCommandResponse>) {
        val correlationHeader = message.headers["correlationId"]
        val correlationId: String? = when (correlationHeader) {
            is String -> correlationHeader
            is ByteArray -> try {
                String(correlationHeader, Charsets.UTF_8)
            } catch (e: Exception) {
                null
            }
            else -> null
        }
        if (correlationId == null) {
            log.warn("Received console reply without correlationId; ignoring.")
            return
        }

        val future: CompletableFuture<ConsoleCommandResponse>? = this.pendingReplies.remove(correlationId)
        if (future == null) {
            log.warn("No pending future for console reply correlationId={}", correlationId)
            return
        }
        future.complete(message.payload)
    }
}