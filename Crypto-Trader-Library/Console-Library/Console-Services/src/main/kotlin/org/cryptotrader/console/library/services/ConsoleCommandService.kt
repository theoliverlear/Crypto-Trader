package org.cryptotrader.console.library.services

import org.cryptotrader.console.library.communication.request.ConsoleCommandRequest
import org.cryptotrader.console.library.communication.response.ConsoleCommandResponse
import org.springframework.stereotype.Service

@Service
class ConsoleCommandService {
    fun executeCommand(command: ConsoleCommandRequest): ConsoleCommandResponse {
        // TODO: Implement this.
        return ConsoleCommandResponse("The event chain succeeded.", null)
    }
}