package org.cryptotrader.health.library.component

import org.cryptotrader.health.library.model.CryptoTraderService
import org.cryptotrader.health.library.service.HealthCheckService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class HealthCheckScheduler(private val healthCheckService: HealthCheckService) {

    private val log = LoggerFactory.getLogger(HealthCheckScheduler::class.java)

    @Scheduled(fixedRate = 60000)
    fun checkAllServices() {
        log.info("Starting health check cycle for all services...")
        CryptoTraderService.entries.forEach { service ->
            if (service != CryptoTraderService.HEALTH &&
                service != CryptoTraderService.ADMIN &&
                service != CryptoTraderService.VERSION) {
                this.healthCheckService.checkAndPersist(service)
            }
        }
        log.info("Health check cycle complete.")
    }
}
