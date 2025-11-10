package org.cryptotrader.api.library.communication.request

data class TradeRequest(
    val currencyCode: String,
    val numDollars: Double,
    val numShares: Double,
    val vendor: String
)
