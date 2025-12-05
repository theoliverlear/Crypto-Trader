package org.cryptotrader.console

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["org.cryptotrader.console.library.component",
    "org.cryptotrader.console.library.services",
    "org.cryptotrader.console.library.model",
    "org.cryptotrader.console.library.communication",
    "org.cryptotrader.console.library.events",
    "org.cryptotrader.universal.library.component"])
open class CryptoTraderConsoleApplication

fun main(args: Array<String>) {
    System.setProperty("cryptotrader.load.currency", "false")
    runApplication<CryptoTraderConsoleApplication>(*args)
}