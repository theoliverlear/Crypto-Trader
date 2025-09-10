package org.cryptotrader.contact.config

import org.springframework.boot.autoconfigure.mail.MailProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.util.Properties

/**
 * Provides a JavaMailSender bean if Spring Boot's auto-configuration does not create one
 * (for example, when no spring.mail.* properties are defined). This keeps EmailService
 * autowirable in all environments. Values from MailProperties will be applied when present.
 */
@Configuration
@EnableConfigurationProperties(MailProperties::class)
open class MailSenderConfig {

    @Bean
    @Primary
    @ConditionalOnMissingBean(JavaMailSender::class)
    open fun javaMailSender(mailProperties: MailProperties): JavaMailSender {
        val sender = JavaMailSenderImpl()
        mailProperties.host?.let { sender.host = it }
        mailProperties.port?.let { sender.port = it }
        mailProperties.username?.let { sender.username = it }
        mailProperties.password?.let { sender.password = it }
        mailProperties.protocol?.let { sender.protocol = it }

        if (mailProperties.properties.isNotEmpty()) {
            val javaProps = Properties()
            mailProperties.properties.forEach { (key, value) -> javaProps[key] = value }
            sender.javaMailProperties = javaProps
        }
        return sender
    }
}
