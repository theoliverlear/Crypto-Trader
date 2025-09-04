package org.cryptotrader.data;

import org.cryptotrader.component.CurrencyJsonGenerator;
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
        "org.cryptotrader.data",
        "org.cryptotrader.api",
})
@EnableJpaRepositories(basePackages = {
        "org.cryptotrader.repository"
})
public class CryptoTraderDataApplication {
    public static void main(String[] args) {
        boolean loadCurrencies = Boolean.parseBoolean(
                System.getProperty("cryptotrader.loadCurrencies",
                        System.getenv().getOrDefault("CRYPTO_TRADER_LOAD_CURRENCIES", "true"))
        );
        if (loadCurrencies) {
            CurrencyJsonGenerator.standalone().generateAndSave();
        }
        SpringApplication.run(CryptoTraderDataApplication.class, args);
    }
}
