package org.cryptotrader.contact.component

import org.cryptotrader.api.library.component.EventPublisher
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.annotation.Configuration

@ConditionalOnBean(EventPublisher::class)
@Configuration
open class ContactEventPublishers(
    private val eventPublisher: EventPublisher
) {

}