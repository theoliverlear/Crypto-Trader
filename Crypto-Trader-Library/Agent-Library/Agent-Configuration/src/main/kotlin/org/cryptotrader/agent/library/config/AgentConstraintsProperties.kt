package org.cryptotrader.agent.library.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = "agent.constraints")
open class AgentConstraintsProperties {
    var allowedHosts: Set<String> = emptySet()
    var allowedTables: Set<String> = emptySet()
    var allowedRoot: String = "."
}
