package org.cryptotrader.api.library.services

import org.cryptotrader.api.library.communication.request.TradeRequest
import org.cryptotrader.api.library.entity.currency.Currency
import org.cryptotrader.api.library.entity.portfolio.Portfolio
import org.cryptotrader.api.library.entity.portfolio.PortfolioAsset
import org.cryptotrader.api.library.entity.user.ProductUser
import org.springframework.stereotype.Service

@Service
class TradeService(
    private val authContextService: AuthContextService,
    private val portfolioService: PortfolioService,
    private val currencyService: CurrencyService) {
    fun checkout(tradeRequest: TradeRequest): Boolean {
        val isLoggedIn: Boolean = this.authContextService.isUserAuthenticated()
        if (!isLoggedIn) {
            return false
        }
        val productUser: ProductUser = this.authContextService.getAuthenticatedProductUser() ?: return false
        return if (this.isSharesCheckout(tradeRequest)) {
            this.sharesCheckout(tradeRequest, productUser)
        } else {
            this.dollarsCheckout(tradeRequest, productUser)
        }
    }

    private fun isSharesCheckout(tradeRequest: TradeRequest): Boolean {
        return tradeRequest.numShares > 0
    }

    private fun isDollarsCheckout(tradeRequest: TradeRequest): Boolean {
        return tradeRequest.numDollars > 0
    }

    private fun sharesCheckout(tradeRequest: TradeRequest, productUser: ProductUser): Boolean {
        return this.sharesCheckout(tradeRequest, productUser, tradeRequest.numShares)
    }
    
    private fun sharesCheckout(tradeRequest: TradeRequest, productUser: ProductUser, numShares: Double): Boolean {
        val currencyToBuy: Currency = this.currencyService.getCurrencyByCurrencyCode(tradeRequest.currencyCode) ?: return false
        val userPortfolio: Portfolio = productUser.portfolio ?: Portfolio(productUser)
        val portfolioAsset: PortfolioAsset = PortfolioAsset.builder()
                                                           .portfolio(userPortfolio)
                                                           .currency(currencyToBuy)
                                                           .shares(numShares)
                                                           .build()
        portfolioAsset.updateValues()
        userPortfolio.addAsset(portfolioAsset)
        this.portfolioService.savePortfolioAsset(portfolioAsset)
        return true
    }

    private fun dollarsCheckout(tradeRequest: TradeRequest, productUser: ProductUser): Boolean {
        val currencyToBuy: Currency = this.currencyService.getCurrencyByCurrencyCode(tradeRequest.currencyCode) ?: return false
        val numShares: Double = tradeRequest.numDollars / currencyToBuy.value
        return this.sharesCheckout(tradeRequest, productUser, numShares)
    }
}