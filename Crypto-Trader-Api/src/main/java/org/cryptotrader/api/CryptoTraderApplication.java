package org.cryptotrader.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.cryptotrader.component.CurrencyJsonGenerator;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EntityScan(basePackages = "org.cryptotrader.entity")
@ComponentScan(basePackages = {
        "org.cryptotrader.api",
        "org.cryptotrader.component"
})
public class CryptoTraderApplication {
    public static void main(String[] args) {
        CurrencyJsonGenerator.standalone().generateAndSave();
        SpringApplication.run(CryptoTraderApplication.class, args);
    }
}
