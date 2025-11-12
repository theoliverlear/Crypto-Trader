package org.cryptotrader.api.library.services

import org.cryptotrader.api.library.entity.portfolio.Portfolio
import org.cryptotrader.api.library.entity.portfolio.PortfolioAsset
import org.cryptotrader.api.library.entity.portfolio.PortfolioAssetHistory
import org.cryptotrader.api.library.entity.trade.TradeEvent
import org.cryptotrader.api.library.entity.user.ProductUser
import org.cryptotrader.api.library.repository.TradeEventRepository
import org.springframework.stereotype.Service

@Service
class TradeEventService(
    val tradeEventRepository: TradeEventRepository,
    val portfolioService: PortfolioService
) {
    fun saveTradeEvent(tradeEvent: TradeEvent): TradeEvent {
        return this.tradeEventRepository.save(tradeEvent)
    }

    fun getAssetByTradeEvent(tradeEvent: TradeEvent): PortfolioAsset {
        val assetHistory: PortfolioAssetHistory = tradeEvent.assetHistory
        return assetHistory.portfolioAsset
    }

    fun getAllByProductUser(productUser: ProductUser): List<TradeEvent> {
        return this.tradeEventRepository.findAllByPortfolioId(productUser.portfolio.id)
    }
}