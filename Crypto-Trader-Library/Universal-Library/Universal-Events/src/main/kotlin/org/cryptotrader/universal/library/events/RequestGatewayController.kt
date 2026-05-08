package org.cryptotrader.universal.library.events

import org.cryptotrader.universal.library.events.model.EventBinding
import org.cryptotrader.universal.library.events.model.RequestGateway
import org.springframework.messaging.Message
import java.time.Duration
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

abstract class RequestGatewayController<Request, Reply> :
    RequestGateway<Request, Reply> {
    abstract val pendingReplies: MutableMap<String, CompletableFuture<Reply>>
    abstract val eventPublisher: EventPublisher

    fun getCorrelationId(): String {
        return UUID.randomUUID().toString()
    }

    fun getInitialHeaders(correlationId: String, authorizationHeader: String?): Map<String, Any> {
        val headers = mutableMapOf<String, Any>()
        if (!authorizationHeader.isNullOrBlank()) {
            headers["Authorization"] = authorizationHeader
        }
        headers["correlationId"] = correlationId
        return headers
    }

    fun getFutureReply(): CompletableFuture<Reply> {
        return CompletableFuture<Reply>()
    }

    fun setPendingReply(correlationId: String, future: CompletableFuture<Reply>) {
        this.pendingReplies[correlationId] = future
    }

    fun tryExecuteAndWait(future: CompletableFuture<Reply>, correlationId: String, timeout: Duration): Reply {
        return try {
            future.get(timeout.toMillis(), TimeUnit.MILLISECONDS)
        } catch (exception: TimeoutException) {
            this.pendingReplies.remove(correlationId)
            throw exception
        } catch (exception: Exception) {
            this.pendingReplies.remove(correlationId)
            throw exception
        }
    }

    fun handleReply(message: Message<Reply>) {
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
            return
        }
        val future: CompletableFuture<Reply>? = this.pendingReplies.remove(correlationId)
        future?.complete(message.payload)
    }

    override fun execute(
        binding: EventBinding,
        request: Request,
        timeout: Duration,
        authorizationHeader: String?
    ): Reply {

        val correlationId: String = this.getCorrelationId()
        val future = this.getFutureReply()
        this.setPendingReply(correlationId, future)
        val headers = this.getInitialHeaders(correlationId, authorizationHeader)
        this.eventPublisher.publish(binding.bindingName, request, headers)

        return try {
            future.get(timeout.toMillis(), TimeUnit.MILLISECONDS)
        } catch (exception: TimeoutException) {
            this.pendingReplies.remove(correlationId)
            throw exception
        } catch (exception: Exception) {
            this.pendingReplies.remove(correlationId)
            throw exception
        }
    }
}