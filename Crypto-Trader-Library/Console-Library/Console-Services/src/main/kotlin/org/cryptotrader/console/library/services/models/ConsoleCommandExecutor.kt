package org.cryptotrader.console.library.services.models

import org.cryptotrader.console.library.communication.response.ConsoleCommandResponse

interface ConsoleCommandExecutor {
    fun executeCommand(command: String): ConsoleCommandResponse
}