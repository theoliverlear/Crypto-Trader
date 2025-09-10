package org.cryptotrader.data;

import org.cryptotrader.api.library.component.CurrencyJsonGenerator;
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
@EntityScan(basePackages = "org.cryptotrader.api.library.entity")
@ComponentScan(basePackages = {
        "org.cryptotrader.api.library.component",
        "org.cryptotrader.data",
        "org.cryptotrader.api.library",
})
@EnableJpaRepositories(basePackages = {
        "org.cryptotrader.api.library.repository"
})
public class CryptoTraderDataApplication {
    public static void main(String[] args) {
        boolean loadCurrencies = shouldLoadCurrencies();
        if (loadCurrencies) {
            CurrencyJsonGenerator.standalone().generateAndSave();
        }
        // Rely on standard Spring Boot externalized configuration (application.yml)
        SpringApplication.run(CryptoTraderDataApplication.class, args);
    }

    private static boolean shouldLoadCurrencies() {
        String loadCurrenciesEnv = System.getenv().getOrDefault("CRYPTO_TRADER_LOAD_CURRENCIES", "true");
        String loadCurrenciesSetting = System.getProperty("cryptotrader.loadCurrencies", loadCurrenciesEnv);
        boolean loadCurrencies = Boolean.parseBoolean(loadCurrenciesSetting);
        return loadCurrencies;
    }
}
