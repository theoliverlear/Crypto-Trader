package org.cryptotrader.contact.service.email

import io.github.oshai.kotlinlogging.KotlinLogging
import org.cryptotrader.contact.comm.email.response.EmailResponse
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

val log = KotlinLogging.logger {  }

@Service
class EmailService(
    private val emailSender: JavaMailSender
) {
    fun send(email: EmailResponse) {
        log.info { "Sending email to ${email.to}" }
        val message = this.emailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, false, Charsets.UTF_8.name())
        helper.setTo(email.to)
        helper.setSubject(email.subject)
        helper.setText(email.body, false)
        this.emailSender.send(message)
    }


}