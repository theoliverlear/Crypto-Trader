package org.cryptotrader.logging.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "crypto-trader-logging.level")
public class CryptoTraderLoggingProperties {
    /**
     * The logging level for the file appender. Defaults to TRACE.
     */
    private String file = "TRACE";

    /**
     * The logging level for the console appender. Defaults to INFO.
     */
    private String console = "INFO";
}
