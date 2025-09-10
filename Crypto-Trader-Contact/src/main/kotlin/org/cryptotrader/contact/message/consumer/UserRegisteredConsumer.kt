package org.cryptotrader.contact.message.consumer

import io.github.oshai.kotlinlogging.KotlinLogging
import org.cryptotrader.api.library.events.UserRegisteredEvent
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Consumer

private val log = KotlinLogging.logger {  }

@Configuration
open class UserRegisteredConsumer {
    
    @Bean
    open fun userRegistered(): Consumer<UserRegisteredEvent> {
        log.info { "User registered consumer." }
        return Consumer { event ->
            log.info { "User registered: ${event.dateTime}" }
        }
    }
}