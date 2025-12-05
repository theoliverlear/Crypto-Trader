package org.cryptotrader.console.library.services

import org.cryptotrader.api.library.communication.response.CurrencyNamesResponse
import org.cryptotrader.console.library.communication.response.ConsoleCommandResponse
import org.cryptotrader.data.library.entity.currency.Currency
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

    private fun isShowCommand(command: String): Boolean {
        return command.contains("currency show")
    }
    
    fun executeCommand(command: String): ConsoleCommandResponse {
        when {
            isListCommand(command) -> {
                return this.executeListCommand()
            }
            isShowCommand(command) -> {
                return this.executeShowCommand(command)
            }
        }
        return ConsoleCommandResponse("The event chain succeeded.", null)
    }


    private fun executeShowCommand(command: String): ConsoleCommandResponse {
        val commandParts: List<String> = command.split(" ")
        if (commandParts.size != 3) {
            return ConsoleCommandResponse("Invalid command format.", null)
        }
        val currencyCode: String = commandParts[2].uppercase()
        val currency: Currency = this.currencyService.getCurrencyByCurrencyCode(currencyCode)
        return ConsoleCommandResponse(currency.toString(), currency)
    }
    
    private fun executeListCommand(): ConsoleCommandResponse {
        val currencyNames: List<String> = this.currencyService.getCurrencyNames(true).sorted()
        val namesResponse = CurrencyNamesResponse(currencyNames)
        val consoleString = currencyNames.joinToString(separator = "\n") { it }
        return ConsoleCommandResponse(consoleString, namesResponse)
    }
}