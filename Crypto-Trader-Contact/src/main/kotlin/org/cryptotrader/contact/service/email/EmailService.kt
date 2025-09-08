package org.cryptotrader.contact.service.email

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.mail.internet.MimeMessage
import org.cryptotrader.contact.comm.email.request.EmailRequest
import org.cryptotrader.contact.service.email.template.Template
import org.cryptotrader.contact.service.email.template.TemplateService
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

val log = KotlinLogging.logger {  }

@Service
class EmailService(
    private val emailSender: JavaMailSender,
    private val templateService: TemplateService
) {
    fun send(email: EmailRequest) {
        when (email.template) {
            Template.WELCOME -> this.sendWelcome(email)
        }
    }

    private fun sendWelcome(email: EmailRequest) {
        log.info { "Sending email to ${email.to}" }
        val message: MimeMessage = this.getDefaultMessage()
        val helper = this.getDefaultHelper(message)
        val templateVariableMap = mapOf(
            "body" to email.body,
            "subject" to email.subject,
            "name" to "User",
            "ctaLabel" to "Open app",
            "year" to "2025"
        )
        helper.setTo(email.to)
        helper.setSubject(email.subject)
        val html: String = this.templateService.process(email.template, templateVariableMap)
        helper.setText(html, true)
        this.emailSender.send(message)
    }

    private fun getDefaultHelper(message: MimeMessage): MimeMessageHelper =
        MimeMessageHelper(message, false, Charsets.UTF_8.name())

    private fun getDefaultMessage(): MimeMessage = this.emailSender.createMimeMessage()
}