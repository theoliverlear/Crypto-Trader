package org.cryptotrader.health.library.config

import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties

@AutoConfiguration
@EnableConfigurationProperties(HealthCheckProperties::class)
open class HealthAutoConfig
