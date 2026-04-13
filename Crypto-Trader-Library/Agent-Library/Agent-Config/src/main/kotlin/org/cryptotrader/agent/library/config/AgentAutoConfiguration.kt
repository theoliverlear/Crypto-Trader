package org.cryptotrader.agent.library.config

import org.cryptotrader.universal.library.config.HttpClientConfig
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.annotation.Import

@AutoConfiguration
@Import(HttpClientConfig::class)
class AgentAutoConfiguration
