package org.cryptotrader.simulator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = [
    "org.cryptotrader.simulator.library.config",
    "org.cryptotrader.simulator.library.services",
    "org.cryptotrader.simulator.library.events",
    "org.cryptotrader.data.library.repository",
])
class CryptoTraderSimulatorApplication

fun main(args: Array<String>) {
    runApplication<CryptoTraderSimulatorApplication>(*args)
}