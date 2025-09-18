package org.cryptotrader.api.library.events.publisher

import org.cryptotrader.api.library.component.EventPublisher
import org.cryptotrader.api.library.events.UserRegisteredEvent
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.stereotype.Component

@ConditionalOnBean(EventPublisher::class)
@Component
class UserEventsPublisher(
    private val eventPublisher: EventPublisher
) {
    fun publishUserRegisteredEvent(registerEvent: UserRegisteredEvent) {
        this.eventPublisher.publish("userRegistered-out-0", registerEvent)
    }
}