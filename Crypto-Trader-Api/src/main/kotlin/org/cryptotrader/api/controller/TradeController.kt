package org.cryptotrader.api.controller

import org.cryptotrader.api.library.communication.request.TradeRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/trade")
class TradeController {
    @PostMapping("/checkout")
    fun checkout(tradeRequest: TradeRequest) {
        
    }
}