@file:JvmName("CryptoTraderService")
package org.cryptotrader.health.models

enum class CryptoTraderService(val port: Int) {
    ANALYSIS(8000),
    API(8080),
    DATA(8085),
    ENGINE(8086),
    DOCS(443);
    companion object {
        const val PREFIX = "Crypto Trader"
    }
    
    override fun toString(): String {
        val normalizedName = this.name.lowercase().replaceFirstChar { it.uppercase() }
        return "$PREFIX $normalizedName"
    }
}