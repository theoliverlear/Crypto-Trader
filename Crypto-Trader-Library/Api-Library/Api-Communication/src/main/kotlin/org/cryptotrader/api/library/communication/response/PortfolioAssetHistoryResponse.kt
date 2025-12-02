package org.cryptotrader.api.library.communication.response

import org.cryptotrader.api.library.entity.portfolio.PortfolioAssetHistory

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
    val lastUpdated: String
) {
    constructor(portfolioAssetHistory: PortfolioAssetHistory) : this(
        portfolioAssetHistory.currency.currencyCode,
        portfolioAssetHistory.shares,
        portfolioAssetHistory.sharesValueInDollars,
        portfolioAssetHistory.assetWalletDollars,
        portfolioAssetHistory.targetPrice,
        portfolioAssetHistory.valueChange,
        portfolioAssetHistory.sharesChange,
        portfolioAssetHistory.isTradeOccurred,
        portfolioAssetHistory.vendor.name,
        portfolioAssetHistory.vendor.rate,
        portfolioAssetHistory.lastUpdated.toString()
    )
}