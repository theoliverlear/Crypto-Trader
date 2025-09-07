package org.cryptotrader.contact.service.email

import com.icegreen.greenmail.util.GreenMail
import com.icegreen.greenmail.util.ServerSetup
import org.assertj.core.api.Assertions
import org.cryptotrader.contact.comm.email.response.EmailResponse
import org.cryptotrader.test.CryptoTraderTest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource

@SpringBootTest
class EmailServiceTest @Autowired constructor(
    private val emailService: EmailService
) : CryptoTraderTest() {
    companion object {
        private val greenMail =
            GreenMail(ServerSetup(0, null, ServerSetup.PROTOCOL_SMTP))

        @JvmStatic @BeforeAll
        fun beforeAll() {
            this.greenMail.start()
        }

        @JvmStatic @AfterAll
        fun afterAll() {
            this.greenMail.stop()
        }

        @JvmStatic
        @DynamicPropertySource
        fun mailProps(registry: DynamicPropertyRegistry) {
            registry.add("spring.mail.host") { "localhost" }
            registry.add("spring.mail.port") { this.greenMail.smtp.port }
            registry.add("spring.mail.properties.mail.smtp.auth") { "false" }
            registry.add("spring.mail.properties.mail.smtp.starttls.enable") { "false" }
            registry.add("spring.mail.properties.mail.smtp.ssl.enable") { "false" }
            registry.add("spring.mail.test-connection") { "false" }
            registry.add("spring.mail.username") { "" }
            registry.add("spring.mail.password") { "" }
            registry.add("management.health.mail.enabled") { "false" }
        }
    }

    @Test
    fun `Should send email with valid request`() {
        val response = EmailResponse(
            "Testing Crypto Trader",
            "Hello from Spring Boot in Kotlin",
            "test@local"
        )
        this.emailService.send(response)
        val messages = greenMail.receivedMessages
        Assertions.assertThat(messages).hasSize(1)
        Assertions.assertThat(messages[0].subject).isEqualTo("Testing Crypto Trader")
        Assertions.assertThat(messages[0].allRecipients[0].toString()).isEqualTo("test@local")
        Assertions.assertThat(messages[0].content.toString()).isEqualTo(
            "Hello from Spring Boot in Kotlin"
        )
    }
}