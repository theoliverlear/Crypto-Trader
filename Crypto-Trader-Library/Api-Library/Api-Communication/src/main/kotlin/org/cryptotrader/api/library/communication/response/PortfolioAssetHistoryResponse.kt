package org.cryptotrader.api.library.communication.response

import java.time.LocalDateTime

data class PortfolioAssetHistoryResponse (
    val currencyCode: String,
    val shares: Double,
    val sharesValueInDollars: Double,
    val assetWalletDollars: Double,
    val targetPrice: Double,
    val valueChange: Double,
    val sharesChange: Double,
    val tradeOccurred: Boolean,
    val vendorName: String,
    val vendorRate: Double,
    val lastUpdated: LocalDateTime
)