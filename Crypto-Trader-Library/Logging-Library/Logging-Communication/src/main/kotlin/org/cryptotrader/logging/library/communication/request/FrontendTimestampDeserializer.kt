package org.cryptotrader.logging.library.communication.request

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class FrontendTimestampDeserializer : JsonDeserializer<LocalDateTime>() {

    companion object {
        private val FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss")
        private val CENTRAL_ZONE: ZoneId = ZoneId.of("America/Chicago")
    }

    override fun deserialize(parser: JsonParser, context: DeserializationContext): LocalDateTime {
        val text = parser.text
        return try {
            val instant = Instant.parse(text)
            LocalDateTime.ofInstant(instant, CENTRAL_ZONE)
        } catch (_: Exception) {
            LocalDateTime.parse(text, FORMATTER)
        }
    }
}
