package org.cryptotrader.security.config

import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.context.annotation.Import

@AutoConfiguration
@Import(SecurityAutoConfiguration::class)
open class SecurityAutoConfig
