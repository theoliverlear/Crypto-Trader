package org.cryptotrader.api;

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
        "org.cryptotrader.api",
        "org.cryptotrader.api.library.component",
        "org.cryptotrader.api.library.services",
})
@EnableJpaRepositories(basePackages = {
        "org.cryptotrader.api.library.repository"
})
public class CryptoTraderApiApplication {
    public static void main(String[] args) {
        blockCurrencyLoading();
        SpringApplication.run(CryptoTraderApiApplication.class, args);
    }

    private static void blockCurrencyLoading() {
        System.setProperty("cryptotrader.loadCurrencies", "false");
    }
}
