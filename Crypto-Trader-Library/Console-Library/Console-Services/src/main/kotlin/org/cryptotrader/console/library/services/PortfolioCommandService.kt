package org.cryptotrader.console.library.services

import org.cryptotrader.api.library.communication.response.PortfolioResponse
import org.cryptotrader.api.library.entity.portfolio.Portfolio
import org.cryptotrader.api.library.entity.user.ProductUser
import org.cryptotrader.api.library.services.AuthContextService
import org.cryptotrader.api.library.services.PortfolioService
import org.cryptotrader.console.library.communication.response.ConsoleCommandResponse
import org.cryptotrader.console.library.services.models.ConsoleCommandExecutor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
open class PortfolioCommandService @Autowired constructor(
    private val portfolioService: PortfolioService,
    private val authContextService: AuthContextService
) : ConsoleCommandExecutor {
    
    @Transactional(readOnly = true)
    override fun executeCommand(command: String): ConsoleCommandResponse {
        return when {
            isShowCommand(command) -> {
                this.executeShowCommand(command)
            }
            else -> ConsoleCommandResponse("Unknown command: $command")
        }
    }

    private fun isShowCommand(command: String): Boolean {
        return command.contains("portfolio show")
    }

    private fun executeShowCommand(command: String): ConsoleCommandResponse {
        val currentUser: ProductUser = this.authContextService.getAuthenticatedProductUser() ?:
            return ConsoleCommandResponse("No authenticated user found.")
        val userPortfolio: Portfolio = this.portfolioService.getPortfolioByUserId(currentUser.id)
        try {
            userPortfolio.assets?.size
        } catch (_: Exception) {

        }
        return ConsoleCommandResponse(userPortfolio.toString(),
            PortfolioResponse(userPortfolio))
    }
}