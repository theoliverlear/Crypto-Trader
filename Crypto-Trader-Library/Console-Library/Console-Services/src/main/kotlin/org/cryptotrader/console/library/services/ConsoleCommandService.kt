package org.cryptotrader.console.library.services

import org.cryptotrader.console.library.communication.request.ConsoleCommandRequest
import org.cryptotrader.console.library.communication.response.ConsoleCommandResponse
import org.cryptotrader.console.library.model.SupportedConsoleCommand
import org.cryptotrader.console.library.model.ConsoleHelpCommand
import org.cryptotrader.console.library.model.ConsoleHelpSection
import org.cryptotrader.console.library.infrastructure.annotation.CommandHelp
import org.cryptotrader.console.library.model.exception.CommandNotSupportedException
import org.cryptotrader.console.library.scripts.formatConsoleHelp
import org.cryptotrader.console.library.services.models.ConsoleCommandExecutor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.aop.support.AopUtils
import org.springframework.stereotype.Service

@Service
class ConsoleCommandService @Autowired constructor(
    private val commandParser: ConsoleCommandParser,
    private val currencyCommandService: CurrencyCommandService,
    private val portfolioCommandService: PortfolioCommandService,
    private val commandExecutors: List<ConsoleCommandExecutor>
) {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(ConsoleCommandService::class.java)
    }

    fun executeCommand(command: ConsoleCommandRequest): ConsoleCommandResponse {
        val defaultResponse =
            ConsoleCommandResponse("Unknown command: ${command.commandText}")
        val response: ConsoleCommandResponse = try {
            when (this.commandParser.parseCommand(command.commandText)) {
                SupportedConsoleCommand.HELP -> ConsoleCommandResponse(this.buildHelpText())
                SupportedConsoleCommand.CURRENCY -> this.currencyCommandService.executeCommand(command.commandText)
                SupportedConsoleCommand.PORTFOLIO -> this.portfolioCommandService.executeCommand(command.commandText)
                else -> {
                    defaultResponse
                }
            }
        } catch (exception: CommandNotSupportedException) {
            log.error("Unsupported command entered: ${exception.message}")
            defaultResponse
        }
        return response
    }

    private fun buildHelpText(): String {
        val helpSections = this.commandExecutors.sortedBy { AopUtils.getTargetClass(it).simpleName }.mapNotNull { commandService ->
            val targetClass: Class<*> = AopUtils.getTargetClass(commandService)
            val helpAnnotation: CommandHelp = targetClass.getAnnotation(CommandHelp::class.java) ?: return@mapNotNull null
            val methodHelp: List<ConsoleHelpCommand> = targetClass.declaredMethods
                .filter { method -> method.isAnnotationPresent(CommandHelp::class.java) }
                .sortedBy { method ->
                    method.getAnnotation(CommandHelp::class.java).command
                }
                .map { method ->
                    val methodAnnotation = method.getAnnotation(CommandHelp::class.java)
                    ConsoleHelpCommand(
                        command = methodAnnotation.command,
                        description = methodAnnotation.description
                    )
                }

            ConsoleHelpSection(
                command = helpAnnotation.command,
                description = helpAnnotation.description,
                subcommands = methodHelp
            )
        }

        return formatConsoleHelp(helpSections)
    }
}
