package org.cryptotrader.contact.component

import org.cryptotrader.api.library.config.EventPublisher
import org.springframework.context.annotation.Configuration

@Configuration
open class ContactEventPublishers(
    private val eventPublisher: EventPublisher
) {

}