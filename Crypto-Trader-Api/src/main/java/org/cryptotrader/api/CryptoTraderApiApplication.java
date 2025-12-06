package org.cryptotrader.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EntityScan(basePackages = {
        "org.cryptotrader.api.library.entity",
        "org.cryptotrader.data.library.entity",
        "org.cryptotrader.security.library.entity"
})
@ComponentScan(basePackages = {
        "org.cryptotrader.api",
        "org.cryptotrader.api.library.component",
        "org.cryptotrader.api.library.config",
        "org.cryptotrader.api.library.services",
        "org.cryptotrader.api.library.infrastructure",
        "org.cryptotrader.data.library.services",
        "org.cryptotrader.data.library.services.harvest",
        "org.cryptotrader.data.library.component",
        "org.cryptotrader.security.library.service",
        "org.cryptotrader.console.library.component",
        "org.cryptotrader.universal.library.component"
})
public class CryptoTraderApiApplication {
    public static void main(String[] args) {
        blockCurrencyLoading();
        blockCurrencyHarvesting();
        SpringApplication.run(CryptoTraderApiApplication.class, args);
    }

    // TODO: Migrate to a universal module. (Universal-Scripts)
    private static void blockCurrencyLoading() {
        System.setProperty("cryptotrader.load.currency", "false");
    }
    
    private static void blockCurrencyHarvesting() {
        System.setProperty("cryptotrader.harvest.currency", "false");
    }
}
