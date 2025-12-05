package org.cryptotrader.console.library.services

import org.cryptotrader.api.library.communication.response.CurrencyNamesResponse
import org.cryptotrader.console.library.communication.response.ConsoleCommandResponse
import org.cryptotrader.data.library.services.CurrencyService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CurrencyCommandService @Autowired constructor(
    private val currencyService: CurrencyService
) {
    private fun isListCommand(command: String): Boolean {
        return command == "currency list"
    }

    fun executeCommand(command: String): ConsoleCommandResponse {
        when {
            isListCommand(command) -> {
                val currencyNames: List<String> = this.currencyService.getCurrencyNames(false)
                val namesResponse: CurrencyNamesResponse = CurrencyNamesResponse(currencyNames)
                val consoleString = currencyNames.joinToString(separator = "\n") { it }
                return ConsoleCommandResponse(consoleString, namesResponse)
            }
        }
        return ConsoleCommandResponse("The event chain succeeded.", null)
    }
}