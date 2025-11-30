package org.cryptotrader.api.controller.websocket

import com.sigwarthsoftware.springboot.websocket.WebSocketHandler
import org.cryptotrader.data.library.entity.currency.Currency
import org.cryptotrader.data.library.services.CurrencyService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

private val log = LoggerFactory.getLogger(CurrencyValueWebSocketHandler::class.java)

@Component
class CurrencyValueWebSocketHandler(
    private val currencyService: CurrencyService
) : WebSocketHandler<String, String>() {
    override fun makeResponse(currencyRequest: String): String {
        try {
            val currency: Currency = this.currencyService.getCurrencyByCurrencyCode(currencyRequest)
            return currency.value.toString()
        } catch (exception: NullPointerException) {
            log.error("Error fetching currency value for code: $currencyRequest", exception)
            return "0.00"
        }
    }
}