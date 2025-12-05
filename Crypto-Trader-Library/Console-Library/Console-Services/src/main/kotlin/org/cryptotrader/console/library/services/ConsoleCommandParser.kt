package org.cryptotrader.console.library.services

import org.cryptotrader.console.library.model.SupportedConsoleCommand
import org.cryptotrader.console.library.model.exception.CommandNotSupportedException
import org.springframework.stereotype.Service

@Service
class ConsoleCommandParser {
    
    fun parseCommand(command: String): SupportedConsoleCommand {
        if (this.containsSupportedCommand(command)) {
            return this.getCommandType(command)
        }
        throw CommandNotSupportedException(command)
    }
    
    fun containsSupportedCommand(command: String): Boolean {
        val firstWord: String = command.split(" ")[0]
        return firstWord in SupportedConsoleCommand.entries.map { it.commandBase }
    }
    
    fun getCommandType(command: String): SupportedConsoleCommand {
        val firstWord: String = command.split(" ")[0]
        return SupportedConsoleCommand.entries.first { it.commandBase == firstWord }
    }
}