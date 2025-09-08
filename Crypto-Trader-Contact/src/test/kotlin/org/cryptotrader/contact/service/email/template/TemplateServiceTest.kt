package org.cryptotrader.contact.service.email.template

import org.assertj.core.api.Assertions.assertThat
import org.cryptotrader.contact.natives.normalized
import org.cryptotrader.test.CryptoTraderTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals

@SpringBootTest
class TemplateServiceTest @Autowired constructor(
    private val templateService: TemplateService
) : CryptoTraderTest() {
    
    val testTemplate = Template.WELCOME
    val testSubject: String = "Testing Crypto Trader"
    val testBody: String = "Hello from Spring Boot in Kotlin"
    val name: String = "User"
    val ctaLabel: String = "Open app"
    val year: String = "2025"
    
    @Test
    fun `Should properly inject variables`() {
        val expectedHtml: String = getExpectedWelcomeHtml(this.testSubject, this.testBody).normalized()
        val actualHtml: String = this.templateService.process(
            this.testTemplate,
            mapOf(
                "subject" to this.testSubject,
                "body" to this.testBody,
                "name" to this.name,
                "ctaLabel" to this.ctaLabel,
                "year" to this.year
            )
        ).normalized()
        assertEquals(expectedHtml, actualHtml)
    }
}

fun getExpectedWelcomeHtml(subject: String, body: String): String {
    // TODO: Make this more dynamic and less hardcoded.
    return """
            <!DOCTYPE html>
            <html lang="en" dir="ltr">
            <head>
                <meta charset="UTF-8" />
                <title>${subject}</title>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        line-height: 1.6;
                        color: #333;
                        padding: 20px;
                    }
                    .btn {
                        background:#0d6efd;
                        color:#fff;
                        padding:10px 16px;
                        text-decoration:none;
                        border-radius:6px;
                    }
                    .btn:hover {
                        cursor: pointer;
                        background:#0b5ed7;
                    }
                    .text-muted {
                        color: #6c757d;
                        font-size:12px;
                    }
                </style>
            </head>
            <body>
            <h1>Welcome, User!</h1>
            <p>${body}</p>

            <p>
                <a href="" class="btn">Open app</a>
            </p>

            <hr/>
            <img src="../../../Crypto-Trader-Assets/src/main/resources/assets/images/logos/crypto_trader/crypto_trader_logo_cropped_transparent.png" alt="Crypto Trader Logo" width="100" height="100"/>
            <p class="text-muted">
                Sent by Crypto Trader • <span>2025</span>
            </p>
            </body>
            </html>
        """.trimIndent()
}