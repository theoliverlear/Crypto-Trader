package org.cryptotrader.api.controller.websocket

import com.sigwarthsoftware.springboot.websocket.WebSocketHandler
import org.cryptotrader.api.library.entity.currency.Currency
import org.cryptotrader.api.library.services.CurrencyService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

private val log = LoggerFactory.getLogger(CurrencyValueWebSocketHandler::class.java)

@Component
class CurrencyValueWebSocketHandler(
    private val currencyService: CurrencyService
) : WebSocketHandler<String, String>() {
    override fun makeResponse(currencyRequest: String): String {
        val currency: Currency = this.currencyService.getCurrencyByCurrencyCode(currencyRequest)
        return currency.value.toString()
    }
}