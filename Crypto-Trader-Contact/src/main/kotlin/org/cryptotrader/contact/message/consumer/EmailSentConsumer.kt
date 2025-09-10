package org.cryptotrader.contact.message.consumer

import io.github.oshai.kotlinlogging.KotlinLogging
import org.cryptotrader.contact.library.events.EmailSentEvent
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Consumer

private val log = KotlinLogging.logger {  }

@Configuration
open class EmailSentConsumer {

    @Bean
    open fun emailSentEventConsumer(): Consumer<EmailSentEvent> {
        return Consumer { event ->
            log.info { "Email sent: ${event.subject}" }
        }
    }
}