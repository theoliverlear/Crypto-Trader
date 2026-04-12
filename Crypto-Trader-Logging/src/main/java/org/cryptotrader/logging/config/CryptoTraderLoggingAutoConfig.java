package org.cryptotrader.logging.config;

import org.cryptotrader.logging.properties.CryptoTraderLoggingProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnClass(name = "org.qos.logback.classic.Logger")
@EnableConfigurationProperties({CryptoTraderLoggingProperties.class})
public class CryptoTraderLoggingAutoConfig {

    @Bean
    @ConditionalOnProperty(prefix = "cryptotrader.exceptions", name = "enabled", havingValue = "true", matchIfMissing = true)
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }
}
