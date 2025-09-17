package org.cryptotrader.security.library.event

import org.cryptotrader.security.library.entity.BannedIpAddress
import org.cryptotrader.security.library.repository.BannedIpAddressesRepository
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import java.time.LocalDateTime

class SecurityEventLogger(
    private val repository: BannedIpAddressesRepository
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @EventListener
    fun recordIpEvent(ipAddress: String) {
        try {
            val record = BannedIpAddress()
            record.ipAddress = ipAddress
            record.occurredAt = LocalDateTime.now()
            this.repository.save(record)
        } catch (ex: Exception) {
            this.log.warn("Failed to persist security event: {}", ex.toString())
        }
    }
}
