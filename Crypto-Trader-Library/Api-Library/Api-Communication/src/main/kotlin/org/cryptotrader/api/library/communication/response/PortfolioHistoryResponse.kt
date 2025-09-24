package org.cryptotrader.api.library.communication.response

import java.time.LocalDateTime

data class PortfolioHistoryResponse(
    val dollarBalance: Double,
    val shareBalance: Double,
    val totalWorth: Double,
    val valueChange: Double,
    val tradeOccurred: Boolean,
    val lastUpdated: LocalDateTime
)
