package org.cryptotrader.universal.library.events.model

import java.time.Duration

interface RequestGateway<Request, Reply> {
    fun execute(binding: EventBinding, request: Request): Reply
    fun execute(binding: EventBinding, request: Request, timeout: Duration): Reply
    fun execute(binding: EventBinding, request: Request, timeout: Duration, authorizationHeader: String?): Reply
}