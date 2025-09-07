@file:JvmName("EmailController")
package org.cryptotrader.contact.controller

import org.cryptotrader.contact.comm.email.response.EmailResponse
import org.cryptotrader.contact.service.email.EmailService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/contact/email")
class EmailController(
    private val emailService: EmailService
) {
    @GetMapping("")
    fun sendEmail(): ResponseEntity<EmailResponse> {
        println("Sending email...")
        try {
            val response = EmailResponse("Testing Crypto Trader", "Hello from Spring Boot in Kotlin", "sigwarthsoftware@gmail.com")
            this.emailService.send(response)
            println("Email sent.")
        } catch (e: Exception) {
            println("Error sending email: ${e.message}")
            return ResponseEntity<EmailResponse>(HttpStatus.INTERNAL_SERVER_ERROR)
        }
        return ResponseEntity<EmailResponse>(HttpStatus.OK)
    }
}