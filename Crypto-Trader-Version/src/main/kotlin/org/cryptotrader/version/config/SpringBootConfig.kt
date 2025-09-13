package org.cryptotrader.version.config

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(
    basePackages = ["org.cryptotrader.version", "org.cryptotrader.desktop.library.component"]
)
open class SpringBootConfig