package org.cryptotrader.security

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(SecurityAutoConfiguration::class)
open class CryptoTraderSecurityApplication

fun main(args: Array<String>) {
    runApplication<CryptoTraderSecurityApplication>(*args)
}