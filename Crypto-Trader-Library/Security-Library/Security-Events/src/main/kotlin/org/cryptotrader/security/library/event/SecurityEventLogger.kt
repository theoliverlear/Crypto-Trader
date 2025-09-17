package org.cryptotrader.security.library.event

import org.cryptotrader.security.library.entity.BannedIp
import org.cryptotrader.security.library.repository.SecurityEventRepository
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import java.time.LocalDateTime

class SecurityEventLogger(
    private val repository: SecurityEventRepository
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @EventListener
    fun recordIpEvent(ipAddress: String) {
        try {
            val record = BannedIp()
            record.ip = ipAddress
            record.occurredAt = LocalDateTime.now()
            this.repository.save(record)
        } catch (ex: Exception) {
            this.log.warn("Failed to persist security event: {}", ex.toString())
        }
    }
}
