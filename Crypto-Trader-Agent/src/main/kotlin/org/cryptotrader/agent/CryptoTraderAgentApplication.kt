package org.cryptotrader.agent

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CryptoTraderAgentApplication

fun main(args: Array<String>) {
    runApplication<CryptoTraderAgentApplication>(*args)
}
