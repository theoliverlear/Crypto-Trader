package org.cryptotrader.api.library.services

import org.cryptotrader.api.library.entity.trade.TradeEvent
import org.cryptotrader.api.library.repository.TradeEventRepository
import org.springframework.stereotype.Service

@Service
class TradeEventService(
    val tradeEventRepository: TradeEventRepository
) {
    fun saveTradeEvent(tradeEvent: TradeEvent): TradeEvent {
        return this.tradeEventRepository.save(tradeEvent)
    }
}