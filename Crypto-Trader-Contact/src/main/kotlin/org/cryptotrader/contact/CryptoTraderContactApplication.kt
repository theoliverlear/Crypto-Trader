package org.cryptotrader.contact

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["org.cryptotrader.contact.component",
                               "org.cryptotrader.api.library.component"])
open class CryptoTraderContactApplication

fun main(args: Array<String>) {
    runApplication<CryptoTraderContactApplication>(*args)
}