package org.cryptotrader.console.library.model.exception

class CommandNotSupportedException (
    command: String
): Exception(MESSAGE + command) {
    companion object {
        private const val MESSAGE = "Command not supported: "
    }
}