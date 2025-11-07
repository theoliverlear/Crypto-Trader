package org.cryptotrader.api.library.services

import org.cryptotrader.api.library.communication.request.TradeRequest
import org.cryptotrader.api.library.entity.user.ProductUser
import org.springframework.stereotype.Service

@Service
class TradeService(
    private val authContextService: AuthContextService,
    private val portfolioService: PortfolioService) {
    fun checkout(user: ProductUser, tradeRequest: TradeRequest) {
        
    }
}