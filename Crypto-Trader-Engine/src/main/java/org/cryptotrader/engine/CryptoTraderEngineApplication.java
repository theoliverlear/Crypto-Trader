package org.cryptotrader.engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

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
        // TODO: Require health check of Crypto-Trader-Data before launch.

        boolean tradingEnabled = Boolean.parseBoolean(
                System.getProperty("cryptotrader.engine.trading.enabled",
                        System.getenv().getOrDefault("CRYPTO_TRADER_ENGINE_TRADING_ENABLED", "true"))
        );

        if (!tradingEnabled) {
            System.setProperty("spring.task.scheduling.enabled", "false");
        }
        SpringApplication.run(CryptoTraderEngineApplication.class, args);
    }
}
