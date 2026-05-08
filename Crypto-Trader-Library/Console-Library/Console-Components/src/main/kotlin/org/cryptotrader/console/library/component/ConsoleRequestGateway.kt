package org.cryptotrader.console.library.component

import org.cryptotrader.console.library.communication.request.ConsoleCommandRequest
import org.cryptotrader.console.library.communication.response.ConsoleCommandResponse
import org.cryptotrader.universal.library.events.EventPublisher
import org.cryptotrader.universal.library.events.RequestGatewayController
import org.cryptotrader.universal.library.events.model.EventBinding
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
    override val eventPublisher: EventPublisher
) : RequestGatewayController<ConsoleCommandRequest, ConsoleCommandResponse> (

) {
    private val log: Logger = LoggerFactory.getLogger(ConsoleRequestGateway::class.java)
    override val pendingReplies: MutableMap<String, CompletableFuture<ConsoleCommandResponse>> =
        ConcurrentHashMap()

    override fun execute(binding: EventBinding, request: ConsoleCommandRequest): ConsoleCommandResponse {
        return this.execute(binding, request, Duration.ofSeconds(15))
    }

    override fun execute(
        binding: EventBinding,
        request: ConsoleCommandRequest,
        timeout: Duration
    ): ConsoleCommandResponse {
        return this.execute(binding, request, timeout, null)
    }

    override fun execute(
        binding: EventBinding,
        request: ConsoleCommandRequest,
        timeout: Duration,
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
            request
        )

        this.eventPublisher.publish(ConsoleEventBinding.CONSOLE_REQUESTS.bindingName, request, headers)

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

    fun handleConsoleReply(message: Message<ConsoleCommandResponse>) {
        log.info("Handling console reply correlationId={}", message.headers["correlationId"])
        this.handleReply(message)
    }
}