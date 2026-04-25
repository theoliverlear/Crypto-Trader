package org.cryptotrader.logging

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class CryptoTraderLoggingApplication

fun main(args: Array<String>) {
    runApplication<CryptoTraderLoggingApplication>(*args)
}