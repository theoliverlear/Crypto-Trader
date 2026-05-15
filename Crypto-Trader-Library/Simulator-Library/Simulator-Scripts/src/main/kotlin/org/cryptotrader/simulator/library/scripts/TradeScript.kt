package org.cryptotrader.simulator.library.scripts

import org.cryptotrader.api.library.entity.portfolio.Portfolio
import org.cryptotrader.api.library.entity.portfolio.PortfolioAsset
import org.cryptotrader.api.library.model.trade.AssetTrader
import org.cryptotrader.api.library.model.trade.TradeContext
import org.cryptotrader.data.library.entity.currency.CurrencyHistory

fun getSimulatedAsset(portfolioAsset: PortfolioAsset, filteredCurrencyValues: MutableList<CurrencyHistory>): PortfolioAsset {
    val initialPrice: Double = filteredCurrencyValues.firstOrNull()?.value ?: portfolioAsset.currency.value
    portfolioAsset.targetPrice = initialPrice
    val trader = AssetTrader(portfolioAsset, TradeContext.SIMULATION)

    val isFirstPurchase: Boolean =
        portfolioAsset.shares == 0.0 && portfolioAsset.assetWalletDollars > 0.0
    if (isFirstPurchase && filteredCurrencyValues.isNotEmpty()) {
        trader.buy(initialPrice)
        portfolioAsset.targetPrice = initialPrice
    }

    filteredCurrencyValues.forEach { currencyHistory ->
        trader.trade(currencyHistory.value)
    }

    val lastHistory: CurrencyHistory? = filteredCurrencyValues.lastOrNull()
    if (lastHistory != null) {
        portfolioAsset.currency.value = lastHistory.value
        portfolioAsset.currency.lastUpdated = lastHistory.lastUpdated
    }
    portfolioAsset.updateValues()
    return portfolioAsset
}

fun getSimulatedPortfolio(
    portfolio: Portfolio,
    filteredCurrencyValues: Map<String, MutableList<CurrencyHistory>>
): Portfolio {
    val simulatedAssets: List<PortfolioAsset> =
        portfolio.assets.parallelStream()
            .map { portfolioAsset ->
                val filteredCurrencyValuesForAsset: MutableList<CurrencyHistory>? =
                    filteredCurrencyValues[portfolioAsset.currency.currencyCode]
                requireNotNull(filteredCurrencyValuesForAsset) {
                    "Currency history not found for asset: ${portfolioAsset.currency.currencyCode}"
                }
                getSimulatedAsset(
                    portfolioAsset,
                    filteredCurrencyValuesForAsset
                )
            }
            .toList()

    portfolio.assets = simulatedAssets.toMutableList()
    return portfolio
}