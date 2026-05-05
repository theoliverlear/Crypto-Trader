package org.cryptotrader.simulator.library.scripts

import org.cryptotrader.api.library.entity.portfolio.PortfolioAsset
import org.cryptotrader.api.library.entity.trade.TradeEvent
import org.cryptotrader.api.library.model.trade.AssetTrader
import org.cryptotrader.data.library.entity.currency.Currency
import org.cryptotrader.data.library.entity.currency.CurrencyHistory

fun getSimulatedAsset(portfolioAsset: PortfolioAsset, filteredCurrencyValues: MutableList<CurrencyHistory>): PortfolioAsset {
    val trader = AssetTrader(portfolioAsset)
    val tradeEvents: MutableList<TradeEvent> = mutableListOf<TradeEvent>()
    filteredCurrencyValues.forEach { currencyHistory ->
        val currency: Currency = currencyHistory.currency
        portfolioAsset.currency = currency
        trader.trade()
    }
    return portfolioAsset
}
