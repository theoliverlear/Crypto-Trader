package org.cryptotrader.console.library.services

import org.cryptotrader.console.library.communication.request.ConsoleCommandRequest
import org.cryptotrader.console.library.communication.response.ConsoleCommandResponse
import org.cryptotrader.console.library.model.SupportedConsoleCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ConsoleCommandService @Autowired constructor(
    private val commandParser: ConsoleCommandParser,
    private val currencyCommandService: CurrencyCommandService,
    private val portfolioCommandService: PortfolioCommandService
) {
    fun executeCommand(command: ConsoleCommandRequest): ConsoleCommandResponse {
        return when (commandParser.parseCommand(command.command)) {
            SupportedConsoleCommand.HELP -> ConsoleCommandResponse("The event chain succeeded.")
            SupportedConsoleCommand.CURRENCY -> this.currencyCommandService.executeCommand(command.command)
            SupportedConsoleCommand.PORTFOLIO -> this.portfolioCommandService.executeCommand(command.command)
            else -> ConsoleCommandResponse("Unknown command: $command")
        }
    }
}