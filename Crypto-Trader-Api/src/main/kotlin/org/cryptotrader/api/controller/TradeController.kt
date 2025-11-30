package org.cryptotrader.api.controller

import org.cryptotrader.api.library.communication.request.TradeRequest
import org.cryptotrader.api.library.communication.response.OperationSuccessfulResponse
import org.cryptotrader.api.library.services.AuthContextService
import org.cryptotrader.api.library.services.TradeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/trade")
class TradeController(
    private val tradeService: TradeService,
    private val authContextService: AuthContextService
){
    @PostMapping("/checkout")
    fun checkout(@RequestBody tradeRequest: TradeRequest): ResponseEntity<OperationSuccessfulResponse> {
        val isLoggedIn: Boolean = this.authContextService.isUserAuthenticated()
        if (!isLoggedIn) {
            return ResponseEntity(OperationSuccessfulResponse(false), HttpStatus.UNAUTHORIZED)
        }
        val tradeSuccessful: Boolean = this.tradeService.checkout(tradeRequest)
        return ResponseEntity.ok(OperationSuccessfulResponse(tradeSuccessful))
    }
}