package org.theoliverlear;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.theoliverlear.component.CurrencyJsonGenerator;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class CryptoTraderApplication {
    public static void main(String[] args) {
        CurrencyJsonGenerator.standalone().generateAndSave();
        SpringApplication.run(CryptoTraderApplication.class, args);
    }
}
