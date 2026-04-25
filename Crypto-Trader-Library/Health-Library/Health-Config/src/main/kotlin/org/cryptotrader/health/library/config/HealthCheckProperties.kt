package org.cryptotrader.health.library.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "ct.health")
data class HealthCheckProperties(
    val intervalSeconds: Long = 60,
    val enabled: Boolean = true
)
