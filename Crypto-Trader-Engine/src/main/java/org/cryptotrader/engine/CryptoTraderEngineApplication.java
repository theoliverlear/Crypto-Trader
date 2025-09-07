package org.cryptotrader.engine;

import org.cryptotrader.health.models.CryptoTraderService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import static org.cryptotrader.health.ServiceStatusChecker.isServiceAlive;


/**
 * Main entry point for the CryptoTrader Engine application. This class
 * initializes and configures the Spring Boot application with support for
 * asynchronous processing, scheduling, JPA repositories, and entity scanning.
 * Additionally, it provides mechanisms to enable or disable trading based on
 * external configuration through environment variables or system properties.
 *
 * @author theoliverlear - Oliver Lear Sigwarth
 * @see org.springframework.boot.autoconfigure.SpringBootApplication
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
@EntityScan(basePackages = "org.cryptotrader.entity")
@ComponentScan(basePackages = {
        "org.cryptotrader.component",
        "org.cryptotrader.engine",
        "org.cryptotrader.api",
})
@EnableJpaRepositories(basePackages = {
        "org.cryptotrader.repository"
})
public class CryptoTraderEngineApplication {
    public static void main(String[] args) {
        boolean tradingEnabled = isTradingEnabled();
        if (!tradingEnabled) {
            disableTrading();
        }
        SpringApplication.run(CryptoTraderEngineApplication.class, args);
    }

    private static void disableTrading() {
        System.setProperty("spring.task.scheduling.enabled", "false");
    }

    private static boolean isTradingEnabled() {
        String tradingEnabledEnv = System.getenv().getOrDefault("CRYPTO_TRADER_ENGINE_TRADING_ENABLED", "true");
        String tradingEnabledProperty = System.getProperty("cryptotrader.engine.trading.enabled", tradingEnabledEnv);
        boolean tradingEnabled = Boolean.parseBoolean(tradingEnabledProperty);
        return tradingEnabled;
    }

    @Component
    @Profile("!docs")
    static class EngineStartupVerifier implements ApplicationRunner {
        @Override
        public void run(ApplicationArguments args) {
            boolean dataServiceAvailable = isServiceAlive(CryptoTraderService.DATA);
            if (!dataServiceAvailable) {
                throw new IllegalStateException(
                                "Crypto-Trader-Data service is not available. " +
                                "Please start it before launching Crypto-Trader-Engine.");
            }
        }
    }
}
